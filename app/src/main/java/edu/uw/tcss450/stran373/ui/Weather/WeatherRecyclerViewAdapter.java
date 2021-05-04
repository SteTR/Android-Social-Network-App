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
import edu.uw.tcss450.stran373.databinding.FragmentWeatherCardBinding;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherViewHolder>{

    private final List<WeatherCard> mWeathers;

    private final Map<WeatherCard, Boolean> mExpandedFlags;

    public WeatherRecyclerViewAdapter(List<WeatherCard> items) {
        this.mWeathers = items;
        this.mExpandedFlags = mWeathers.stream()
                .collect(Collectors.toMap(Function.identity(),weather -> false));
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder(LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.fragment_weather_card, parent, false));
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

        public WeatherViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentWeatherCardBinding.bind(view);
            binding.buttonMore.setOnClickListener(this::handleMoreOrLess);
        }

        private void handleMoreOrLess(final View button) {
            mExpandedFlags.put(mWeather, !mExpandedFlags.get(mWeather));
            displayPreview();
        }

        private void displayPreview() {
            if (mExpandedFlags.get(mWeather)) {
                binding.textCurrentTemp.setVisibility(View.GONE);
                binding.imageCurrent.setVisibility(View.GONE);
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

        void setWeather(final WeatherCard weather) {
            mWeather = weather;
            binding.textLocation.setText(weather.getLocation());
            binding.textCurrentTemp.setText(weather.getCurrentTemp());
            binding.textDate1.setText(weather.getDate1());
            binding.textDate2.setText(weather.getDate2());
            binding.textDate3.setText(weather.getDate3());
            binding.textDate4.setText(weather.getDate4());
            binding.textDate5.setText(weather.getDate5());
            binding.textTemperature1.setText(weather.getTemp1());
            binding.textTemperature2.setText(weather.getTemp2());
            binding.textTemperature3.setText(weather.getTemp3());
            binding.textTemperature4.setText(weather.getTemp4());
            binding.textTemperature5.setText(weather.getTemp5());
            displayPreview();
        }
    }
}
