package io.mosaicnetworks.chatr.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    List<Contact> getAll();

    @Query("SELECT * FROM contact WHERE pubKeyHex IN (:userIds)")
    List<Contact> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM contact WHERE user_name LIKE :first AND "
            + "user_name LIKE :last LIMIT 1")
    Contact findByUserName(String first, String last);

    @Insert
    void insertAll(Contact... contacts);

    @Delete
    void delete(Contact contacts);
}
