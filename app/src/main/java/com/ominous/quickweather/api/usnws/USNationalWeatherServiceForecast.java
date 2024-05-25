/*
 *   Copyright 2019 - 2024 Tyler Williamson
 *
 *   This file is part of QuickWeather.
 *
 *   QuickWeather is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   QuickWeather is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with QuickWeather.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ominous.quickweather.api.usnws;

public class USNationalWeatherServiceForecast {
    public GridpointForecast properties;

    public static class GridpointForecast {
        public String units;
        public String forecastGenerator;
        public String generatedAt;
        public String updateTime;
        public QuantitativeValue elevation;
        public GridpointForecastPeriod[] periods;

        public static class GridpointForecastPeriod {
            public int number;
            public String name;
            public String startTime;
            public String endTime;
            public Boolean isDaytime;
            public QuantitativeValue temperature; // need to set "forecast_temperature_qv" feature flag
            public String temperatureTrend;

            public QuantitativeValue probabilityOfPrecipitation;
            public QuantitativeValue dewpoint;
            public QuantitativeValue relativeHumidity;
            public QuantitativeValue windSpeed; // need to set "forecast_wind_speed_qv" feature flag
            public String windDirection;
            public String shortForecast;
            public String detailedForecast;

        }
    }

    public static class QuantitativeValue {
        public double value;
        public double maxValue;
        public double minValue;
        public String unitCode;
        public String qualityControl;
    }
}
