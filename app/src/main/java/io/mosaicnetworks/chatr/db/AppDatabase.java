package io.mosaicnetworks.chatr.db;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executors;

import io.mosaicnetworks.chatr.Utils;
import mobile.Mobile;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ContactDao contactDao();

    public synchronized static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "contacts-database")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).contactDao().insertAll(populateData());
                            }
                        });
                    }
                })
                .allowMainThreadQueries()
                .build();
    }


    public static Contact populateData() {
        Contact contact = new Contact();
        contact.setUserName("Me!");
        String keyPair = Mobile.getPrivPublKeys();    //publicKey[!@#$%^]privateKey
        String[] separated =  keyPair.split("=!@#@!=");
        contact.setPubKeyHex(separated[0].trim());
        contact.setNetAddr(Utils.getIPAddress(true) + ":6666");
        return contact;
    }

}

