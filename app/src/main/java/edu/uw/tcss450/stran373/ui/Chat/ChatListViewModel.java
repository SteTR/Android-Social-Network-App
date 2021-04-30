package edu.uw.tcss450.stran373.ui.Chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ChatListViewModel extends AndroidViewModel {

    private MutableLiveData<List<ChatCard>> mCardList;

    public ChatListViewModel(@NonNull final Application application) {
        super(application);
        mCardList = new MutableLiveData<>();
        mCardList.setValue(new ArrayList<>());
    }


}
