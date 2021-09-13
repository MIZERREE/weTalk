package com.ree.mizer.wetalk.room;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MessageDAO {

    @Insert
    void insert(Message message);

    @Query("SELECT * FROM  messages")
    LiveData<List<Message>> getAllMessages();

}
