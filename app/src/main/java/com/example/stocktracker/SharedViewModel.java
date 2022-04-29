package com.example.stocktracker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> stock = new MutableLiveData<String>();
    private final MutableLiveData<String> search = new MutableLiveData<String>();


    public void setStock(String ticker) {
        stock.setValue(ticker);
    }

    public LiveData<String> getStock() {

        return stock;
    }

    //TODO: if we implement layout changes to and from portrait we will have to store search bars and maybe table lists
    public void setSearchTerm(String term) {
        search.setValue(term);
    }

    public LiveData<String> getSearchTerm() {
        return search;
    }
}