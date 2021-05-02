package edu.uw.tcss450.stran373.ui.Chat.Card;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.stran373.ui.Chat.Card.ChatCard;

public class ChatListViewModel extends AndroidViewModel {

    private final MutableLiveData<List<ChatCard>> mCardList;

    public ChatListViewModel(@NonNull final Application application) {
        super(application);
        mCardList = new MutableLiveData<>();
        mCardList.setValue(new ArrayList<>());
        mCardList.setValue(generateChatCards());
    }

    public void addChatListObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<ChatCard>> observer) {
        mCardList.observe(owner, observer);
    }

    public List<ChatCard> getCardList()
    {
        return mCardList.getValue();
    }

    /**
     * Temporary method to generate chat cards in random amounts ranging from 1 to 10.
     * @return a list of chat cards
     */
    private static List<ChatCard> generateChatCards()
    {
        final List<ChatCard> chatCards = new ArrayList<>();
        for (int i = 0; i < Math.random() * 10 + 1; i++)
        {
            ChatCard tempCard = new ChatCard.Builder("", "", null, i).build();
            chatCards.add(tempCard);
        }
        return chatCards;
    }
}