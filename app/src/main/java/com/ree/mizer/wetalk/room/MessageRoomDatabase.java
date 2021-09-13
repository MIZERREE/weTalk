package com.ree.mizer.wetalk.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Message.class, version = 1)
public abstract class MessageRoomDatabase extends RoomDatabase {

    //singleton object of room database
    private static volatile MessageRoomDatabase messageRoomInstance;

    static MessageRoomDatabase getDatabase(final Context context) {
        if (messageRoomInstance == null) {
            synchronized (MessageRoomDatabase.class) {
                if (messageRoomInstance == null) {
                    messageRoomInstance = Room.databaseBuilder(context.getApplicationContext(), MessageRoomDatabase.class, "message_database").build();
                }
            }
        }
        return messageRoomInstance;
    }

    public abstract MessageDAO messageDAO();

}
