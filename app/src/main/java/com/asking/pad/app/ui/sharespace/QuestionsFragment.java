package com.asking.pad.app.ui.sharespace;

import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;

/**
 * 回答广场
 */

public class QuestionsFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    public static QuestionsFragment newInstance() {
        QuestionsFragment fragment = new QuestionsFragment();
        return fragment;
    }
}
