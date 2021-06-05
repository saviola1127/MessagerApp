package com.savypan.italker.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.savypan.italker.factory.model.db.Group;
import com.savypan.italker.factory.model.db.Group_Table;
import com.savypan.italker.factory.model.db.Session;
import com.savypan.italker.factory.model.db.Session_Table;

public class SessionHelper {

    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
