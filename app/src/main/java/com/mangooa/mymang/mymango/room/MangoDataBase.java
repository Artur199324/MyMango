package com.mangooa.mymang.mymango.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Entity.class}, version = 1)
public abstract class MangoDataBase extends RoomDatabase {
    public abstract DaoTab daoTab();
}
