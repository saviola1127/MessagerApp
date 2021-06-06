package com.savypan.italker.factory.data.helper;

import android.util.Log;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.savypan.italker.factory.model.db.AppDatabase;
import com.savypan.italker.factory.model.db.Group;
import com.savypan.italker.factory.model.db.GroupMember;
import com.savypan.italker.factory.model.db.Group_Table;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.model.db.Session;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * 数据库存储的工具类
 * 辅助完成：增删改，不处理查的操作
 */
public class DBHelper {
    private static final DBHelper instance;

    //观察者集合
    //class<?> 观察的表
    // set<?> 每一个表对应的观察者有很多
    private final Map<Class<?>, Set<IChangedListener>> changedListenerMap = new HashMap<>();

    static {
        instance = new DBHelper();
    }

    private DBHelper() {

    }


    private <Model extends BaseModel> Set<IChangedListener> getIChangedListenerSet(Class<Model> tClass) {
        if (changedListenerMap.containsKey(tClass)) {
            return instance.changedListenerMap.get(tClass);
        }
        return null;
    }


    /***
     * 添加一个监听器 对某个表观注
     * @param tClass
     * @param listener
     * @param <Model>
     */
    public static <Model extends BaseModel> void addChangedListener(final Class<Model> tClass,
                                                                    IChangedListener<Model> listener) {
        Set<IChangedListener> changedListeners = instance.getIChangedListenerSet(tClass);
        if (changedListeners == null) {
            //初始化容器
            changedListeners = new HashSet<>();
            //添加到总的map
            instance.changedListenerMap.put(tClass, changedListeners);
        }

        changedListeners.add(listener);
    }


    /***
     * 删除某一个表的监听器
     * @param tClass
     * @param listener
     * @param <Model>
     */
    public static <Model extends BaseModel> void removeChangedListener(final Class<Model> tClass,
                                                                    IChangedListener<Model> listener) {
        Set<IChangedListener> changedListeners = instance.getIChangedListenerSet(tClass);
        if (changedListeners == null) {
            //容器为空代表根本没有直接返回
            return;
        }
        //从容器中删除监听者
        changedListeners.remove(tClass);
    }


    //限定条件是BaseModel
    public static<Model extends BaseModel> void save(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                adapter.saveAll(Arrays.asList(models));
                instance.notifySaving(tClass, models);
            }
        }).build().execute();
    }


    public static<Model extends BaseModel> void delete(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                adapter.deleteAll(Arrays.asList(models));
                instance.notifyDeleting(tClass, models);
            }
        }).build().execute();
    }


    private final <Model extends BaseModel> void notifySaving(final Class<Model> tClass,
                                                              final Model... models) {
        //查找监听器
        final Set<IChangedListener> listeners = getIChangedListenerSet(tClass);
        if (listeners != null && listeners.size() > 0) {
            //通用的通知
            for (IChangedListener listener : listeners) {
                listener.onDataSaved(models);
            }
        }

        // TODO 例外情况
        // 群成员变更，需要通知对应群信息更新
        // 消息变化，应该通知会话列表更新
        if (GroupMember.class.equals(tClass)) {
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            updateSession((Message[]) models);
        }

    }

    private final <Model extends BaseModel> void notifyDeleting(final Class<Model> tClass,
                                                                final Model... models) {
        //查找监听器
        final Set<IChangedListener> listeners = getIChangedListenerSet(tClass);
        if (listeners != null && listeners.size() > 0) {
            //通用的通知
            for (IChangedListener listener : listeners) {
                listener.onDataDeleted(models);
            }
        }

        // TODO 例外情况
        // 群成员变更，需要通知对应群信息更新
        // 消息变化，应该通知会话列表更新
        if (GroupMember.class.equals(tClass)) {
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            updateSession((Message[]) models);
        }
    }


    /***
     * 从成员中找出成员所属的群，并对群进行更新
     * @param members
     */
    private void updateGroup(GroupMember... members) {
        //不重复的set集合
        final Set<String> groupIds = new HashSet<>();
        for (GroupMember member : members) {
            //添加群ID
            groupIds.add(member.getGroup().getId());
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                //找到需要通知的群
                List<Group> groups = SQLite.select()
                        .from(Group.class)
                        .where(Group_Table.id.in(groupIds))
                        .queryList();
                instance.notifySaving(Group.class, groups.toArray(new Group[0]));
            }
        }).build().execute();
    }



    /***
     * 从消息列表中找到对应的会话，并对会话进行更新
     * @param messages
     */
    private void updateSession(Message... messages) {
        //标识一个session的唯一性
        final Set<Session.Identity> identities = new HashSet<>();
        for (Message message : messages) {
            Session.Identity identity = Session.createSessionIdentity(message);
            identities.add(identity);
        }
        if (identities.size() == 0) {
            return;
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {

                ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
                Session[] sessions = new Session[identities.size()];
                int index = 0;

                for (Session.Identity identity : identities) {
                    Session session = SessionHelper.findFromLocal(identity.id);
                    if (session == null) {
                        //第一次聊天，创建一个你和对方的会话
                        session = new Session(identity);
                    }
                    //把会话刷新到当前message的最新状态
                    session.refreshToNow();
                    adapter.save(session);

                    //添加到集合
                    sessions[index++] = session;
                }

                instance.notifySaving(Session.class, sessions);
            }
        }).build().execute();
    }


    /***
     * 通知监听器 负责往外部回调数据结果刷新UI
     * @param <T>
     */
    public interface IChangedListener<T extends BaseModel> {
        void onDataSaved(T... list);
        void onDataDeleted(T... list);
    }
}
