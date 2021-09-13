package com.ree.mizer.wetalk.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class Message {

    @PrimaryKey
    @NonNull
    private String id;

    @NonNull
    @ColumnInfo(name = "message")
    private String text;

    public Message() {
    }

    public Message(@NonNull String id, @NonNull String text) {
        this.id = id;
        this.text = text;
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getText() {
        return this.text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

}
