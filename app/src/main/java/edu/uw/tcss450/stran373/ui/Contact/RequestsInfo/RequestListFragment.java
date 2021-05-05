package edu.uw.tcss450.stran373.ui.Contact.RequestsInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentContactListBinding;

/**
 * This class will be used when we retrieve the data from the endpoints
 * @author Andrew Bennett
 */
public class RequestListFragment extends Fragment {

    private RequestListViewModel mModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(RequestListViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactListBinding binding = FragmentContactListBinding.bind(getView());

        /*
        mModel.addContactListObserver(getViewLifecycleOwner(), requestList -> {
            if (!requestList.isEmpty()) {
                binding.recyclerView.setAdapter(
                        new RequestCardRecyclerViewAdapter(requestList));
            }
        });*/
    }
}
