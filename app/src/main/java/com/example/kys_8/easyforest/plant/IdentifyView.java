package com.example.kys_8.easyforest.plant;

import com.example.kys_8.easyforest.ui.IView;

/**
 * Created by kys-8 on 2018/11/2.
 */

public interface IdentifyView extends IView {
    void plantResult(IdentifyResult result);
    void reGetToken();
}
