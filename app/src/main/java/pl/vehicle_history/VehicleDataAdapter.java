package pl.vehicle_history;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pl.vehicle_history.fragment.TimelineFragment;
import pl.vehicle_history.fragment.VehicleInfoFragment;

/**
 * Created by m4lysh on 2015-03-17.
 */
public class VehicleDataAdapter extends FragmentPagerAdapter {

    private static final int VEHICLE_INFO_POS = 0;
    private static final int TIMELINE_POS = 1;

    public VehicleDataAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;

        if (VEHICLE_INFO_POS == i) {
            fragment = new VehicleInfoFragment();
        } else if (TIMELINE_POS == i) {
            fragment = new TimelineFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
