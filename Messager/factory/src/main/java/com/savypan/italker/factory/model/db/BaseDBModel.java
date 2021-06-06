package com.savypan.italker.factory.model.db;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.savypan.italker.factory.utils.DiffUiDataCallback;

public abstract class BaseDBModel<Model> extends BaseModel implements DiffUiDataCallback.UiDataDiffer<Model> {

}
