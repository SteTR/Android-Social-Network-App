package edu.uw.tcss450.stran373.ui.Weather;

import android.graphics.drawable.Icon;
import android.util.Log;
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

        public HourlyViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentHourlyCardBinding.bind(view);
        }

        /**
         * Helper method used to update the weather icons for the 24-hour forecast.
         *
         * @param theCard is an hourly card representing each consecutive hour.
         */
        private void updateHourlyIcon(HourlyCard theCard) {
            int hour = theCard.getTimeNum();
            if (theCard.getCond() < 233) {
                binding.imageCurrent.setImageResource(R.drawable.ic_thunder);
            } else if (theCard.getCond() < 532 && theCard.getCond() > 299) {
                binding.imageCurrent.setImageResource(R.drawable.ic_rainy_24dp);
            } else if (theCard.getCond() < 623 && theCard.getCond() > 599) {
                binding.imageCurrent.setImageResource(R.drawable.ic_snowy_24dp);
            } else if ((theCard.getCond() < 782 && theCard.getCond() > 700) || theCard.getCond() > 800) {
                nightOrDay(hour, new int[] {R.drawable.ic_cloudy_night_24dp, R.drawable.ic_cloudy});
            } else if (theCard.getCond() == 800){
                nightOrDay(hour, new int[] {R.drawable.ic_night_24dp, R.drawable.ic_day});
            }
        }

        /**
         * Helper method to handle the different day times.
         *
         * @param theHour
         * @param theResources
         */
        private void nightOrDay(int theHour, int[] theResources) {
            boolean night = theHour > 16 || theHour < 4;
            if (night) {
                binding.imageCurrent.setImageResource(theResources[0]);
            } else {
                binding.imageCurrent.setImageResource(theResources[1]);
            }
        }

        /**
         * Used to set base text.
         * @param theHourly HourlyCard that is used to reference
         */
        public void setHourly(final HourlyCard theHourly) {
            binding.textTime.setText(theHourly.getTime());
            binding.textTemp.setText(theHourly.getTemp());
            for (int i = 0; i < mHourlys.size(); i++) {
                updateHourlyIcon(mHourlys.get(i));
            }
        }
    }
}
