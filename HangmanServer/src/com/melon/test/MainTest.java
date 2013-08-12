package com.melon.test;

import java.sql.SQLException;

import com.melon.services.CategoriesService;
import com.melon.services.PurchaseService;

public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			System.out.println(CategoriesService.getAllCetogiries().size());
			System.out.println(PurchaseService.getAllPurchase().size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
