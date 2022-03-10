package com.mangooa.mymang.mymango.room;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

@androidx.room.Entity(tableName = "mySaveMango")
public class Entity {

    @ColumnInfo(name = "mangoSave")
    public String mangoSave;

    @ColumnInfo(name = "chapterMangoSave")
    public String chapterMangoSave;

    @ColumnInfo(name = "pageNumber")
    public int pageNumber;

    @ColumnInfo(name = "position")
    public int position;

    @PrimaryKey(autoGenerate = true)
    public int id;

    public Entity(String mangoSave) {
        this.mangoSave = mangoSave;
    }

    public String getChapterMangoSave() {
        return chapterMangoSave;
    }

    public void setChapterMangoSave(String chapterMangoSave) {
        this.chapterMangoSave = chapterMangoSave;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
