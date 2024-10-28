package com.app.orderfoodapp.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.orderfoodapp.API.StoreAPI;
import com.app.orderfoodapp.API.StoreOKAPI;
import com.app.orderfoodapp.Model.Store;
import com.app.orderfoodapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OSMActivity extends AppCompatActivity {
    private MapView map;
    private ImageView btnBack;
    private EditText etSearchAddress;
    private Button btnSearchAddress, btnConfirmAddress;
    private GeoPoint startPoint, endPoint;
    private List<Polyline> polylines = new ArrayList<>();  // Lưu trữ các đối tượng Polyline
    private String searchedAddress;  // Biến lưu địa chỉ mới tìm kiếm
    private double distance; // Biến lưu khoảng cách
    private int storeId; // Lưu storeId
    private String storeAddress; // Địa chỉ của cửa hàng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osmactivity);

        // Đảm bảo osmdroid có quyền truy cập bộ nhớ
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));
        map = findViewById(R.id.map);
        btnBack = findViewById(R.id.btnBack);
        etSearchAddress = findViewById(R.id.etSearchAddress);
        btnSearchAddress = findViewById(R.id.btnSearchAddress);
        btnConfirmAddress = findViewById(R.id.btnConfirmAddress);

        btnBack.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("searchedAddress", searchedAddress);
            resultIntent.putExtra("distance", distance); // Truyền khoảng cách về CartFragment
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnSearchAddress.setOnClickListener(v -> {
            String address = etSearchAddress.getText().toString().trim();
            if (!address.isEmpty()) {
                searchAddress(address);
            } else {
                Toast.makeText(OSMActivity.this, "Please enter an address", Toast.LENGTH_SHORT).show();
            }
        });

        btnConfirmAddress.setOnClickListener(v -> {
            if (startPoint != null) {
                Toast.makeText(OSMActivity.this, "Confirmed address", Toast.LENGTH_SHORT).show();
                // Thực hiện các xử lý khác sau khi xác nhận địa chỉ
            } else {
                Toast.makeText(OSMActivity.this, "Please search and select an address first", Toast.LENGTH_SHORT).show();
            }
        });

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        // Đặt vị trí trung tâm và mức zoom ban đầu
        storeId = getIntent().getIntExtra("storeId", -1);

        if (storeId != -1) {
            // Gọi API để lấy địa chỉ cửa hàng
            getStoreAddress(storeId);
        } else {
            Toast.makeText(this, "Store ID is invalid", Toast.LENGTH_SHORT).show();
        }
        startPoint = new GeoPoint(16.0655, 108.2013); // Sẽ được cập nhật bởi kết quả tìm kiếm
        endPoint = new GeoPoint(16.0544, 108.2022);
        map.getController().setZoom(20.0);
        map.getController().setCenter(startPoint);

        // Thêm lớp la bàn
        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        // Hiển thị đường đi và khoảng cách
        displayRouteAndDistance(startPoint, endPoint);
    }
    private void getStoreAddress(int storeId) {
        StoreOKAPI storeOKAPI = new StoreOKAPI(); // Tạo một thể hiện của StoreOKAPI
        storeOKAPI.getStoreAddress(storeId, new StoreOKAPI.AddressCallback() {
            @Override
            public void onSuccess(String address) {
                // Lấy địa chỉ từ phản hồi
                Log.d("Store Address", address);
                // Gọi hàm để tìm kiếm địa chỉ và gán lat, lon vào endPoint
                searchAddressAndSetEndPoint(address);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(OSMActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchAddressAndSetEndPoint(String address) {
        // Sử dụng Nominatim API hoặc một dịch vụ geocoding khác để tìm kiếm địa chỉ
        String url = "https://nominatim.openstreetmap.org/search?q=" + address + "&format=json&addressdetails=1";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JsonArray jsonArray = new Gson().fromJson(responseData, JsonArray.class);

                    if (jsonArray.size() > 0) {
                        JsonObject location = jsonArray.get(0).getAsJsonObject();
                        double lat = location.get("lat").getAsDouble();
                        double lon = location.get("lon").getAsDouble();

                        // Cập nhật endPoint với lat và lon tìm được
                        endPoint = new GeoPoint(lat, lon);

                        runOnUiThread(() -> {
                            // Cập nhật vị trí trên bản đồ
                            map.getController().setCenter(endPoint);
                            // Có thể hiển thị đường đi từ startPoint đến endPoint
                            displayRouteAndDistance(startPoint, endPoint);
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(OSMActivity.this, "Address not found", Toast.LENGTH_SHORT).show());
                    }
                }
            }
        });
    }

    private void addMarker(GeoPoint point, String title, String description) {
        Marker marker = new Marker(map);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(title);
        marker.setSnippet(description);

        Drawable icon = ContextCompat.getDrawable(this, R.drawable.marker_icon);
        marker.setIcon(icon);

        map.getOverlays().add(marker);
        map.invalidate();
    }

    private void displayRouteAndDistance(GeoPoint startPoint, GeoPoint endPoint) {
        String url = "http://router.project-osrm.org/route/v1/driving/" + startPoint.getLongitude() + "," + startPoint.getLatitude() + ";" + endPoint.getLongitude() + "," + endPoint.getLatitude() + "?overview=full&geometries=geojson";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JsonObject json = new Gson().fromJson(responseData, JsonObject.class);
                    JsonArray coordinates = json.getAsJsonArray("routes").get(0).getAsJsonObject().getAsJsonObject("geometry").getAsJsonArray("coordinates");
                    distance = json.getAsJsonArray("routes").get(0).getAsJsonObject().get("distance").getAsDouble() / 1000.0; // Lưu khoảng cách tính bằng km

                    List<GeoPoint> geoPoints = new ArrayList<>();
                    for (int i = 0; i < coordinates.size(); i++) {
                        JsonArray coord = coordinates.get(i).getAsJsonArray();
                        geoPoints.add(new GeoPoint(coord.get(1).getAsDouble(), coord.get(0).getAsDouble()));
                    }

                    runOnUiThread(() -> {
                        clearOldRoutes();
                        drawRoute(geoPoints);
                        String distanceText = String.format("Distance: %.2f km", distance);
                        Log.d("Distance", distanceText);
                    });
                }
            }
        });
    }


    private void drawRoute(List<GeoPoint> geoPoints) {
        Polyline line = new Polyline();
        line.setPoints(geoPoints);
        polylines.add(line); // Lưu trữ đối tượng Polyline mới
        map.getOverlays().add(line);
        map.invalidate();
    }

    private void clearOldRoutes() {
        // Xóa các đối tượng Polyline cũ khỏi bản đồ
        for (Polyline polyline : polylines) {
            map.getOverlays().remove(polyline);
        }
        polylines.clear(); // Xóa danh sách các đối tượng Polyline cũ
    }

    private void searchAddress(String address) {
        // Sử dụng Nominatim API hoặc một dịch vụ geocoding khác để tìm kiếm địa chỉ
        String url = "https://nominatim.openstreetmap.org/search?q=" + address + "&format=json&addressdetails=1";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JsonArray jsonArray = new Gson().fromJson(responseData, JsonArray.class);

                    if (jsonArray.size() > 0) {
                        JsonObject location = jsonArray.get(0).getAsJsonObject();
                        double lat = location.get("lat").getAsDouble();
                        double lon = location.get("lon").getAsDouble();
                        startPoint = new GeoPoint(lat, lon);
                        searchedAddress = location.get("display_name").getAsString();

                        runOnUiThread(() -> {
                            map.getController().setCenter(startPoint);
                            displayRouteAndDistance(startPoint, endPoint);
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(OSMActivity.this, "Address not found", Toast.LENGTH_SHORT).show());
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }


}

