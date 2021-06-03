package edu.uw.tcss450.stran373.ui.Chat.Conversation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.MainActivity;
import edu.uw.tcss450.stran373.NavGraphDirections;
import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.databinding.ActivityMainBinding;
import edu.uw.tcss450.stran373.databinding.FragmentChatBinding;

/**
 * Create a fragment that represents a chat. Contains the list of messages of one chat
 *
 * @author Steven Tran
 * @author Haoying Li
 */
public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private ChatViewModel mChatViewModel;
    private UserInfoViewModel mUserViewModel;
    private ChatSendViewModel mSendViewModel;

    private int mChatId;
    private String mChatName;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());
        mChatId = args.getChatId();
        mChatName = args.getChatName();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(mChatName);
        actionBar.setDisplayHomeAsUpEnabled(false);

        setHasOptionsMenu(true); // TODO this back button not work as intended, looks like it's resetting it

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserViewModel = provider.get(UserInfoViewModel.class);
        mChatViewModel = provider.get(ChatViewModel.class);
        mChatViewModel.getFirstMessages(mChatId, mUserViewModel.getJwt());
        mSendViewModel = provider.get(ChatSendViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(mChatName);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.chat_settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        Navigation.findNavController(getView()).navigate(NavGraphDirections.actionGlobalNavigationChatList(mChatId));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentChatBinding.bind(getView());

        //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        binding.swipeContainer.setRefreshing(true);

        ChatFragmentArgs chatArguments = ChatFragmentArgs.fromBundle(getArguments());

        final RecyclerView rv = binding.recyclerMessages;

        //Set the Adapter to hold a reference to the list the chat ID that the ChatViewModel holds.
        rv.setAdapter(new ChatRecyclerViewAdapter(
                mChatViewModel.getMessageListByChatId(mChatId),
                mUserViewModel.getEmail()));

        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        binding.swipeContainer.setOnRefreshListener(() -> {
            mChatViewModel.getNextMessages(mChatId, mUserViewModel.getJwt());
        });

        mChatViewModel.addMessageObserver(mChatId, getViewLifecycleOwner(), list -> {
            //inform the RV that the underlying list has (possibly) changed
            rv.getAdapter().notifyDataSetChanged();
            rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
            binding.swipeContainer.setRefreshing(false);
        });

        //Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend2.setOnClickListener(button -> {
            mSendViewModel.sendMessage(mChatId,
                    mUserViewModel.getJwt(),
                    binding.editMessage.getText().toString());
        });

        //when we get the response back from the server, clear the edittext
        mSendViewModel.addResponseObserver(getViewLifecycleOwner(), response -> {
            binding.editMessage.setText("");
        });
    }
}