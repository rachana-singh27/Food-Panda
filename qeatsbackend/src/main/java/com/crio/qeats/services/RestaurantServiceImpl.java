
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;
  /**
   * Get all the restaurants that are open now within a specific service radius.
   * - For peak hours: 8AM - 10AM, 1PM-2PM, 7PM-9PM
   * - service radius is 3KMs.
   * - All other times, serving radius is 5KMs.
   * - If there are no restaurants, return empty list of restaurants.
**/

  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Implement findAllRestaurantsCloseby.
  // Check RestaurantService.java file for the interface contract.
  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {
    
    List<Restaurant> listOfRestaurants;
    // int h = currentTime.getHour();
    // int m = currentTime.getMinute();
    // double latitude = getRestaurantsRequest.getLatitude();
    // double longitude = getRestaurantsRequest.getLongitude();
    
    // if ((h >= 8 && h <= 9) || (h == 10 && m == 0) || (h == 13) || (h == 14 && m == 0)
    //     && (h >= 19 && h <= 20) || (h == 21 && m == 0)) {
      
    //   listOfRestaurants = restaurantRepositoryService
    //   .findAllRestaurantsCloseBy(latitude, longitude, currentTime, peakHoursServingRadiusInKms);
    // } else {

    //   listOfRestaurants = restaurantRepositoryService
    //   .findAllRestaurantsCloseBy(latitude, longitude, currentTime, normalHoursServingRadiusInKms);
    // }

      Double servingRadiusInKms =
        isPeakHour(currentTime) ? peakHoursServingRadiusInKms : normalHoursServingRadiusInKms;

      List<Restaurant> restaurantsCloseBy = restaurantRepositoryService.findAllRestaurantsCloseBy(
    getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(),
    currentTime, servingRadiusInKms);

    GetRestaurantsResponse response = new GetRestaurantsResponse(restaurantsCloseBy);
    log.info(response);
    return response;
  }

  private boolean isTimeWithInRange(LocalTime timeNow,
      LocalTime startTime, LocalTime endTime) {
    return timeNow.isAfter(startTime) && timeNow.isBefore(endTime);
  }

  public boolean isPeakHour(LocalTime timeNow) {
    return isTimeWithInRange(timeNow, LocalTime.of(7, 59, 59), LocalTime.of(10, 00, 01))
        || isTimeWithInRange(timeNow, LocalTime.of(12, 59, 59), LocalTime.of(14, 00, 01))
        || isTimeWithInRange(timeNow, LocalTime.of(18, 59, 59), LocalTime.of(21, 00, 01));
  }

}

