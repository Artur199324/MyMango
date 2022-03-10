package com.mangooa.mymang.mymango.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoTab {

    @Insert
    void entity(Entity entity);

    @Query("SELECT * FROM mySaveMango")
    List<Entity> getEntity();

    @Query("SELECT * FROM mySaveMango WHERE id=:id")
    Entity getEntityId(int id);

    @Query("DELETE FROM mySaveMango WHERE id = :id")
    void deleteById(int id);

    @Query("UPDATE mySaveMango SET chapterMangoSave = :chapterMangoSave, pageNumber= :pageNumber , position= :position WHERE id = :id")
    void update(String chapterMangoSave, int pageNumber, int position, int id);
}
