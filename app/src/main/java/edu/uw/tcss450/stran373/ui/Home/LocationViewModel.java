package edu.uw.tcss450.stran373.ui.Home;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * View Model used to keep track of locations.
 *
 * @author Jonathan Lee
 */
public class LocationViewModel extends ViewModel {

    /**
     * Represents the current location.
     */
    private MutableLiveData<Location> mLocation;

    /**
     * Constructor used to create the view model.
     */
    public LocationViewModel() {
        mLocation = new MediatorLiveData<>();
    }

    /**
     * Used to add an observer for locations.
     *
     * @param theOwner is a LifecycleOwner object.
     * @param theObserver is a location observer.
     */
    public void addLocationObserver(@NonNull LifecycleOwner theOwner,
                                    @NonNull Observer<? super Location> theObserver) {
        mLocation.observe(theOwner, theObserver);
    }

    /**
     * Used to set the current location of the app.
     *
     * @param theLocation is a location.
     */
    public void setLocation(final Location theLocation) {
        if (!theLocation.equals(mLocation.getValue())) {
            mLocation.setValue(theLocation);
        }
    }

    /**
     * Getter method for the current location.
     *
     * @return the current location.
     */
    public Location getLocation() {
        return new Location(mLocation.getValue());
    }
}
