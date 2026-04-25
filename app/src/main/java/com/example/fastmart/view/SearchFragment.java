package com.example.fastmart.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.fastmart.root.MyApplication;
import com.example.fastmart.R;
import com.example.fastmart.model.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchFragment extends Fragment {

    // key for storing search history
    static final String SearchHashKey = "search.history";

    EditText etSearch;
    Button btnSearch;
    ListView lvHistory;
    TextView btnClearAll;
    ImageView ivBack; // 1. Added the Back Arrow variable!

    List<String> historyList;
    ArrayAdapter<String> historyAdapter;
    MyApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app = (MyApplication) requireActivity().getApplication();

        init(view);
        setupListeners(); // NEW: All your click logic is perfectly packaged here!
        loadHistory();
    }

    private void init(View view) {
        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnClearAll = view.findViewById(R.id.btnClearAll);
        lvHistory = view.findViewById(R.id.lvHistory);
        ivBack = view.findViewById(R.id.ivBack); // Initialize the Back Arrow

        historyList = new ArrayList<>();
        historyAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, historyList);
        lvHistory.setAdapter(historyAdapter);
    }

    private void setupListeners() {
        // only hides keyboard,
        ivBack.setOnClickListener(v -> hideKeyboard());

        btnSearch.setOnClickListener(v -> handleSearch());

        // handle phone Keyboard 'Enter'/'Search' Key
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            handleSearch();
            return true;
        });

        //handle Clear History
        btnClearAll.setOnClickListener(v -> clearHistory());
    }

    private void handleSearch() {
        String query = etSearch.getText().toString().trim();

        if (query.isEmpty()) {
            etSearch.setError("Enter something to search");
            return;
        }

        hideKeyboard();
        saveToHistory(query);

        // search through masterProductList
        boolean found = false;
        for (Product p : app.masterProductList) {
            if (p.getName().toLowerCase().contains(query.toLowerCase()) ||
                    p.getModel().toLowerCase().contains(query.toLowerCase())) {
                found = true;
                break;
            }
        }

        if (found) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Search Result")
                    .setMessage("Product Found.")
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            Toast.makeText(requireContext(),
                    "No product found", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToHistory(String query) {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("FastMartPrefs", Context.MODE_PRIVATE);
        Set<String> historySet = new HashSet<>(
                prefs.getStringSet(SearchHashKey, new HashSet<>()));
        historySet.add(query);
        prefs.edit().putStringSet(SearchHashKey, historySet).apply();
        loadHistory();
    }

    private void loadHistory() {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("FastMartPrefs", Context.MODE_PRIVATE);
        Set<String> historySet = prefs.getStringSet(SearchHashKey, new HashSet<>());
        historyList.clear();
        historyList.addAll(historySet);
        historyAdapter.notifyDataSetChanged();
    }

    private void clearHistory() {
        requireActivity()
                .getSharedPreferences("FastMartPrefs", Context.MODE_PRIVATE)
                .edit().remove(SearchHashKey).apply();
        historyList.clear();
        historyAdapter.notifyDataSetChanged();
        Toast.makeText(requireContext(), "History cleared", Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getView() != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }
}