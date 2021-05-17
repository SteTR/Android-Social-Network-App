package edu.uw.tcss450.stran373.ui.Weather;

import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentHourlyCardBinding;

/**
 * Adapter used to bind and change HourlyCards.
 */
public class HourlyRecyclerViewAdapter extends RecyclerView.Adapter<HourlyRecyclerViewAdapter.HourlyViewHolder>{

    /**
    List of all the hourly cards.
     */
    private final List<HourlyCard> mHourlys;

    /**
     * Constructor.
     * @param items HourlyCards being put into the view
     */
    public HourlyRecyclerViewAdapter(List<HourlyCard> items) {
        this.mHourlys = items;
    }

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HourlyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_hourly_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        holder.setHourly(mHourlys.get(position));
    }

    @Override
    public int getItemCount() {
        return mHourlys.size();
    }

    /**
     * Inner class used to interact with individual Cards.
     */
    public class HourlyViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public FragmentHourlyCardBinding binding;
        private HourlyCard mHourly;

        public HourlyViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentHourlyCardBinding.bind(view);
        }

        /**
         * Helper method used to update the weather icons for the 24-hour forecast.
         *
         * @param theCard
         */
        private void updateHourlyIcon(HourlyCard theCard) {
            if (theCard.getCond() <= 232) {
                binding.imageCurrent.setBackgroundResource(R.drawable.ic_thunder);
            } else if (theCard.getCond() <= 531 && theCard.getCond() >= 300) {
                binding.imageCurrent.setBackgroundResource(R.drawable.ic_rainy_24dp);
            } else if (theCard.getCond() <= 622 && theCard.getCond() >= 600) {
                binding.imageCurrent.setBackgroundResource(R.drawable.ic_snowy_24dp);
            } else if ((theCard.getCond() <= 781 && theCard.getCond() >= 701) || theCard.getCond() > 800) {
                binding.imageCurrent.setBackgroundResource(R.drawable.ic_cloudy);
            } else if (theCard.getCond() == 800){
                binding.imageCurrent.setBackgroundResource(R.drawable.ic_day);
            }
        }

        /**
         * Used to set base text.
         * @param theHourly HourlyCard that is used to reference
         */
        void setHourly(final HourlyCard theHourly) {
            mHourly = theHourly;
            binding.textTime.setText(theHourly.getTime());
            binding.textTemp.setText(theHourly.getTemp());
            for (int i = 0; i < mHourlys.size(); i++) {
                updateHourlyIcon(mHourlys.get(i));
            }
        }
    }
}
