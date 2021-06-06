package edu.uw.tcss450.stran373.ui.Weather;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentHourlyCardBinding;

/**
 * Adapter used to bind and change HourlyCards.
 *
 * @author Bryce Fujita
 * @author Jonathan Lee
 */
public class HourlyRecyclerViewAdapter extends RecyclerView.Adapter<HourlyRecyclerViewAdapter.HourlyViewHolder>{

    /**
     * List of all the hourly cards.
     */
    private final List<HourlyCard> mHourlys;

    /**
     * Constructor for the Adapter for the RecyclerView
     * @param theItems HourlyCards being put into the view
     */
    public HourlyRecyclerViewAdapter(List<HourlyCard> theItems) {
        this.mHourlys = theItems;
    }

    /**
     * Used to create a ViewHolder for the HourlyCards.
     *
     * @param theParent is the parent ViewGroup.
     * @param theViewType is the type of view used.
     * @return a new HourlyViewHolder for the 24-hour forecast cards.
     */
    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup theParent, int theViewType) {
        return new HourlyViewHolder(LayoutInflater
                .from(theParent.getContext())
                .inflate(R.layout.fragment_hourly_card, theParent, false));
    }

    /**
     * Displays the data in the ViewHolder at a specified position.
     *
     * @param theHolder is the current ViewHolder.
     * @param thePosition is the specified position.
     */
    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder theHolder, int thePosition) {
        theHolder.setHourly(mHourlys.get(thePosition));
    }

    /**
     * Retrieves the number of hourly cards.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mHourlys.size();
    }

    /**
     * Inner class used to interact with individual Cards.
     */
    public class HourlyViewHolder extends RecyclerView.ViewHolder{

        /**
         * Represents the current View.
         */
        public final View mView;

        /**
         * Represents the current binding of the card.
         */
        public FragmentHourlyCardBinding mBinding;

        /**
         * Constructor used to create the ViewHolder.
         *
         * @param theView is the current View.
         */
        public HourlyViewHolder(View theView) {
            super(theView);
            mView = theView;
            mBinding = FragmentHourlyCardBinding.bind(theView);
        }

        /**
         * Helper method used to update the weather icons for the 24-hour forecast.
         *
         * @param theCard is an hourly card representing each consecutive hour.
         */
        private void updateHourlyIcon(HourlyCard theCard) {
            int hour = theCard.getTimeNum();
            if (theCard.getCond() < 233) {
                mBinding.imageCurrent.setImageResource(R.drawable.ic_thunder);
            } else if (theCard.getCond() < 532 && theCard.getCond() > 299) {
                mBinding.imageCurrent.setImageResource(R.drawable.ic_rainy_24dp);
            } else if (theCard.getCond() < 623 && theCard.getCond() > 599) {
                mBinding.imageCurrent.setImageResource(R.drawable.ic_snowy_24dp);
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
                mBinding.imageCurrent.setImageResource(theResources[0]);
            } else {
                mBinding.imageCurrent.setImageResource(theResources[1]);
            }
        }

        /**
         * Used to set base text.
         * @param theHourly HourlyCard that is used to reference
         */
        public void setHourly(final HourlyCard theHourly) {
            mBinding.textTime.setText(theHourly.getTime());
            mBinding.textTemp.setText(theHourly.getTemp());
            for (int i = 0; i < mHourlys.size(); i++) {
                updateHourlyIcon(mHourlys.get(i));
            }
        }
    }
}
