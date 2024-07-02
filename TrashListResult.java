package com.example.myapplication;

import java.util.ArrayList;

public class TrashListResult {
    String resultCode;
    String resultMsg;
    int numOfRows;
    int pageNo;

    ArrayList<Trash> items = new ArrayList<Trash>();
}
