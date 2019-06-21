package com.example.webbiestest;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * Created by Dheeraj on 14-05-2019.
 * dheerajtiwarri@gmail.com
 */
class ProductRepository {

    private MyDao myDao;
  //  private LiveData<List<MyData>> allMyData;
    private FirebaseFirestore firebaseFirestore;

    ProductRepository(Application application) {
        MyDatabase db = MyDatabase.getDatabase(application);
        myDao = db.myDao();
       // allMyData = myDao.readData();
       // allMyData=new LivePagedListBuilder<>(myDao.readData(),10).build();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    LiveData<PagedList<MyData>> getAllData() {
       // return allMyData;
     return  new LivePagedListBuilder<>(myDao.readData(),5).build();
    }

    void saveData(MyData myData) {
        //insert data direct to room
        new InsertDataInRoomAsyncTask(myDao).execute(myData);

        //send data to fireStore
        sendDataToFireStore(myData);

    }


    //fetch data from fireStore.
    void fetchDataFromFireStore() {

        firebaseFirestore.collection("products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot dataSnapshot : queryDocumentSnapshots) {
                    MyData myData = dataSnapshot.toObject(MyData.class);
                    new InsertAsyncTask(myDao).execute(myData);
                }
            }
        });
    }

    //send data to fireStore.
    public void sendDataToFireStore(MyData myData) {
        firebaseFirestore.collection("products").add(myData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.v(TAG, "Data saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v(TAG, "Fail to Save Data");
            }
        });

    }

    private static class InsertAsyncTask extends AsyncTask<MyData, Void, Void> {

        private MyDao mAsyncTaskDao;

        InsertAsyncTask(MyDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MyData... params) {
            mAsyncTaskDao.addData(params[0]);
            return null;
        }
    }


    private static class InsertDataInRoomAsyncTask extends AsyncTask<MyData, Void, Void> {
        private MyDao myDao;

        public InsertDataInRoomAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(MyData... lists) {
            myDao.addData(lists[0]);

            return null;
        }
    }

}
