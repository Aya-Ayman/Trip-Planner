package com.example.omnia.easytripplanner.database.dao.daoint;

import com.example.omnia.easytripplanner.database.dto.TripDTO;
import java.util.List;

/**
 * Created by EslamWaheed on 3/6/2018.
 */

public interface TripDAOInt {

    TripDTO getTrip(int trip_id, int profile_id);

    TripDTO getTripByName(String name, int profile_id);

    boolean insertTrip(TripDTO tripDTO);

    boolean updateTrip(TripDTO tripDTO);

    boolean deleteTrip(int id);

    int NumberOfTrip();

    List<TripDTO> getAllUpcomingTrips(int profile_id);

    List<TripDTO> getAllDoneAndCancelledTrips(int profile_id);

    public List<String> getAllTripsNames();
}
