package com.ree.mizer.wetalk.room;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MessageViewModel extends AndroidViewModel {

    private final String TAG = this.getClass().getSimpleName();
    private final MessageDAO messageDAO;
    private final MessageRoomDatabase messageDB;
    private final LiveData<List<Message>> liveDataMessages;


    public MessageViewModel(@NonNull Application application) {
        super(application);

        messageDB = MessageRoomDatabase.getDatabase(application);
        messageDAO = messageDB.messageDAO();
        liveDataMessages = messageDAO.getAllMessages();
    }


    public void insert(Message message) {
        new InsertAsyncTask(messageDAO).execute(message);
    }

    public LiveData<List<Message>> getAllMessages() {
        return liveDataMessages;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private class InsertAsyncTask extends AsyncTask<Message, Void, Void> {

        MessageDAO messageDAO;

        public InsertAsyncTask(MessageDAO messageDAO) {
            this.messageDAO = messageDAO;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            messageDAO.insert(messages[0]);
            return null;
        }
    }
}
