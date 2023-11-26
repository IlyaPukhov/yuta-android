package com.ilyap.yuta.ui.fragments;

import static com.ilyap.yuta.utils.UserUtils.getUserId;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.Team;
import com.ilyap.yuta.models.TeamResponse;
import com.ilyap.yuta.ui.carousel.CarouselAdapter;
import com.ilyap.yuta.ui.carousel.ImageModel;
import com.ilyap.yuta.utils.JsonUtils;
import com.ilyap.yuta.utils.RequestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamsFragment extends Fragment {
    ToggleButton managedTeamsButton;
    ToggleButton memberTeamsButton;

    public TeamsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teams, container, false);

        int userId = getUserId(requireActivity());
        TeamResponse teamResponse = JsonUtils.parse(RequestUtils.getTeamsRequest(userId), TeamResponse.class);
        List<Team> managedTeams = teamResponse.getManagedTeams();
        List<Team> othersTeams = teamResponse.getOthersTeams();

        List<ImageModel> carouselList = generateData();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        CarouselAdapter carouselAdapter = new CarouselAdapter(new ArrayList<>(), requireContext());
        recyclerView.setAdapter(carouselAdapter);

        managedTeamsButton = view.findViewById(R.id.manager_button);
        memberTeamsButton = view.findViewById(R.id.member_button);
        managedTeamsButton.setOnClickListener(this::onToggleButtonClick);
        memberTeamsButton.setOnClickListener(this::onToggleButtonClick);

        return view;
    }

    private List<ImageModel> generateData() {
        List<ImageModel> carouselList = new ArrayList<>();

        // Пример данных
        List<Integer> imageList1 = Arrays.asList(R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler);
        List<Integer> imageList2 = Arrays.asList(R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler, R.drawable.photo_filler);

        // Разделим списки изображений на подсписки по 3 элемента в каждом
        List<List<Integer>> pages1 = paginate(imageList1, 2);
        List<List<Integer>> pages2 = paginate(imageList2, 2);

        ImageModel carousel1 = new ImageModel(1, pages1);
        ImageModel carousel2 = new ImageModel(2, pages2);

        // Добавьте другие карусели по аналогии

        carouselList.add(carousel1);
        carouselList.add(carousel2);
        carouselList.add(carousel2);
        carouselList.add(carousel2);
        carouselList.add(carousel2);

        return carouselList;
    }

    private List<List<Integer>> paginate(List<Integer> list, final int pageSize) {
        List<List<Integer>> pages = new ArrayList<>();
        for (int i = 0; i < list.size(); i += pageSize) {
            int end = Math.min(list.size(), i + pageSize);
            pages.add(new ArrayList<>(list.subList(i, end)));
        }
        return pages;
    }

    private void onToggleButtonClick(View view) {
        ToggleButton button = (ToggleButton) view;

        ToggleButton otherButton = (button.getId() == managedTeamsButton.getId())
                ? memberTeamsButton
                : managedTeamsButton;

        button.setTextAppearance(R.style.active_teams);
        button.setChecked(true);
        otherButton.setTextAppearance(R.style.default_teams);
        otherButton.setChecked(false);
    }
}