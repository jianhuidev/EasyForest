package com.example.kys_8.easyforest.weight;

public interface onSearchActionsListener {
    void onItemClicked(String item);
    void showProgress(boolean show);
    void listEmpty();
    void onScroll();
    void error(String localizedMessage);
}
