package edu.uw.tcss450.stran373.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class NewInviteCountViewModel extends ViewModel {
    private MutableLiveData<Integer> mNewInviteCount;

    public NewInviteCountViewModel() {
        mNewInviteCount = new MutableLiveData<>();
        mNewInviteCount.setValue(0);
    }

    public void addInviteCountObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super Integer> observer) {
        mNewInviteCount.observe(owner, observer);
    }

    public void increment() { mNewInviteCount.setValue(mNewInviteCount.getValue() + 1); }

    public void reset() { mNewInviteCount.setValue(0); }
}
