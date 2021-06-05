package com.savypan.italker.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.savypan.italker.factory.model.db.Group;
import com.savypan.italker.factory.model.db.Group_Table;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.model.db.Message_Table;

public class MessageHelper {
    // 从本地找Group
    public static Message findFromLocal(String msgId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(msgId))
                .querySingle();
    }
}
