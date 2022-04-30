package com.example.stocktracker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> stock = new MutableLiveData<String>();
    private final MutableLiveData<String> search = new MutableLiveData<String>();
    private final MutableLiveData<Integer> from = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> to = new MutableLiveData<Integer>();


    //the following two methods help two fragments communicate on which current stocks we are graphing.
    //set method for current stock
    public void setStock(String ticker) {
        stock.setValue(ticker);
    }
    //get method for currect stock
    public LiveData<String> getStock() {
        return stock;
    }

    //This part is used to save information on stock search that the user last made.
    //setter method for search term
    public void setSearchTerm(String term) {
        search.setValue(term);
    }
    //getter method for search term
    public LiveData<String> getSearchTerm() {
        return search;
    }

    //This part is used to save history from the user's travelling inside de app for animations purposes
    //setter method for get index we are coming from
    public void setFrom(int term) { from.setValue(term);}
    //getter method for get index we are coming from
    public LiveData<Integer> getFrom() {
        return from;
    }
    //setter method for get index we are going to
    public void setTo(int term) {
        to.setValue(term);
    }
    //getter method for get index we are going to
    public LiveData<Integer> getTo() {
        return to;
    }


}