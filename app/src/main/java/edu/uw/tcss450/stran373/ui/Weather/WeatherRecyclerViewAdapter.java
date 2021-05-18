package edu.uw.tcss450.stran373.ui.Weather;

import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentWeatherCardBinding;

/**
 * Adapter used to hold Weather cards to the Weather Fragment.
 */
public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherViewHolder>{

    private final List<WeatherCard> mWeathers;

    private final Map<WeatherCard, Boolean> mExpandedFlags;

    private WeatherViewModel mWeatherModel;

    public WeatherRecyclerViewAdapter(List<WeatherCard> items, WeatherViewModel theModel) {
        this.mWeathers = items;
        this.mExpandedFlags = mWeathers.stream()
                .collect(Collectors.toMap(Function.identity(),weather -> false));
        mWeatherModel = theModel;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder(LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.fragment_weather_card, parent, false), mWeatherModel);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.setWeather(mWeathers.get(position));
    }

    @Override
    public int getItemCount() {
        return mWeathers.size();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public FragmentWeatherCardBinding binding;
        private WeatherCard mWeather;

        public WeatherViewHolder(View theView, WeatherViewModel theModel) {
            super(theView);
            mView = theView;
            binding = FragmentWeatherCardBinding.bind(theView);
            binding.buttonMore.setOnClickListener(this::handleMoreOrLess);
            final RecyclerView rv = binding.hourlyRecyler;
            rv.setLayoutManager(
                    new LinearLayoutManager(theView.getContext(),
                            LinearLayoutManager.HORIZONTAL, false));
            rv.setAdapter(new HourlyRecyclerViewAdapter(theModel.getHours()));
        }

        private void handleMoreOrLess(final View theButton) {
            mExpandedFlags.put(mWeather, !mExpandedFlags.get(mWeather));
            displayPreview();
            for (int i = 0; i < mWeathers.size(); i++) {
                updateWeatherCard(mWeathers.get(i));
            }
        }

        /**
         * Helper method used to update the icons for the 5-day forecast
         * depending on the condition.
         *
         * @param theCard is a weather card.
         */
        private void updateWeatherCard(WeatherCard theCard) {
            // First day (today)
            updateIcon(theCard, binding.imageDay1, false);

            // Second day
            updateIcon(theCard, binding.imageDay2, false);

            // Third day
            updateIcon(theCard, binding.imageDay3, false);

            // Fourth day
            updateIcon(theCard, binding.imageDay4, false);

            // Fifth day
            updateIcon(theCard, binding.imageDay5, false);
        }


        /**
         * Helper method used to update the icon of an existing ImageView of a weather card
         *
         * @param theCard is the current weather card.
         * @param theImage is the current image to be altered.
         * @param theCurrent is a boolean to check if the image is for the current weather or not.
         */
        private void updateIcon(WeatherCard theCard, ImageView theImage, boolean theCurrent) {
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            boolean night = (hour > 16 && hour < 24) || (hour > -1 && hour < 4);
            if (theCard.getCurrentCond() <= 232) {
                theImage.setImageResource(R.drawable.ic_thunder);
            } else if (theCard.getCurrentCond() <= 531 && theCard.getCurrentCond() >= 300) {
                theImage.setImageResource(R.drawable.ic_rainy_24dp);
            } else if (theCard.getCurrentCond()<= 622 && theCard.getCurrentCond() >= 600) {
                theImage.setImageResource(R.drawable.ic_snowy_24dp);
            } else if ((theCard.getCurrentCond() <= 781 && theCard.getCurrentCond() >= 701) || theCard.getCurrentCond()> 800) {
                if (night && theCurrent) {
                    theImage.setImageResource(R.drawable.ic_cloudy_night_24dp);
                } else {
                    theImage.setImageResource(R.drawable.ic_cloudy);
                }
            } else if (theCard.getCurrentCond() == 800) {
                if (night && theCurrent) {
                    theImage.setImageResource(R.drawable.ic_night_24dp);
                } else {
                    theImage.setImageResource(R.drawable.ic_day);
                }
            }
        }

        private void displayPreview() {
            if (mExpandedFlags.get(mWeather)) {
                binding.textCurrentTemp.setVisibility(View.GONE);
                binding.imageCurrent.setVisibility(View.GONE);
                binding.hourlyRecyler.setVisibility(View.VISIBLE);
                binding.textDate1.setVisibility(View.VISIBLE);
                binding.textDate2.setVisibility(View.VISIBLE);
                binding.textDate3.setVisibility(View.VISIBLE);
                binding.textDate4.setVisibility(View.VISIBLE);
                binding.textDate5.setVisibility(View.VISIBLE);
                binding.textTemperature1.setVisibility(View.VISIBLE);
                binding.textTemperature2.setVisibility(View.VISIBLE);
                binding.textTemperature3.setVisibility(View.VISIBLE);
                binding.textTemperature4.setVisibility(View.VISIBLE);
                binding.textTemperature5.setVisibility(View.VISIBLE);
                binding.imageDay1.setVisibility(View.VISIBLE);
                binding.imageDay2.setVisibility(View.VISIBLE);
                binding.imageDay3.setVisibility(View.VISIBLE);
                binding.imageDay4.setVisibility(View.VISIBLE);
                binding.imageDay5.setVisibility(View.VISIBLE);
                binding.buttonMore.setImageIcon(
                        Icon.createWithResource(
                                mView.getContext(),
                                R.drawable.ic_less_grey_24dp));
            } else {
                binding.hourlyRecyler.setVisibility(View.GONE);
                binding.textDate1.setVisibility(View.GONE);
                binding.textDate2.setVisibility(View.GONE);
                binding.textDate3.setVisibility(View.GONE);
                binding.textDate4.setVisibility(View.GONE);
                binding.textDate5.setVisibility(View.GONE);
                binding.textTemperature1.setVisibility(View.GONE);
                binding.textTemperature2.setVisibility(View.GONE);
                binding.textTemperature3.setVisibility(View.GONE);
                binding.textTemperature4.setVisibility(View.GONE);
                binding.textTemperature5.setVisibility(View.GONE);
                binding.imageDay1.setVisibility(View.GONE);
                binding.imageDay2.setVisibility(View.GONE);
                binding.imageDay3.setVisibility(View.GONE);
                binding.imageDay4.setVisibility(View.GONE);
                binding.imageDay5.setVisibility(View.GONE);
                binding.buttonMore.setImageIcon(
                        Icon.createWithResource(
                                mView.getContext(),
                                R.drawable.ic_more_grey_24dp));
                binding.textCurrentTemp.setVisibility(View.VISIBLE);
                binding.imageCurrent.setVisibility(View.VISIBLE);
            }
        }

        void setWeather(final WeatherCard theWeather) {
            mWeather = theWeather;
            binding.textLocation.setText(theWeather.getLocation());
            binding.textCurrentTemp.setText(theWeather.getCurrentTemp());
            binding.textDate1.setText(theWeather.getDate1());
            binding.textDate2.setText(theWeather.getDate2());
            binding.textDate3.setText(theWeather.getDate3());
            binding.textDate4.setText(theWeather.getDate4());
            binding.textDate5.setText(theWeather.getDate5());
            binding.textTemperature1.setText(theWeather.getTemp1());
            binding.textTemperature2.setText(theWeather.getTemp2());
            binding.textTemperature3.setText(theWeather.getTemp3());
            binding.textTemperature4.setText(theWeather.getTemp4());
            binding.textTemperature5.setText(theWeather.getTemp5());
            updateIcon(theWeather, binding.imageCurrent, true);
            displayPreview();
        }
    }
}
