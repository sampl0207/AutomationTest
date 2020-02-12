package com.accenture.tests;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.accenture.mobile.Android;
import com.accenture.mobile.screens.CheckoutScreen;
import com.accenture.mobile.screens.HomePage;
import com.accenture.mobile.screens.ProductDetailsScreen;
import com.accenture.mobile.screens.ProductSearchScreen;
import com.accenture.util.Dataset;
import com.accenture.util.DatasetFile;
import com.accenture.util.Settings;

public class TestSearchAndValidate extends BaseTest{

    //Read the TestData file for the items to search
    @DataProvider(name = "item-to-search") 
    public Object[][] dataProviderMethod(){
	DatasetFile datasetFile = new DatasetFile("TestData.xlsx");
	Dataset dataset = datasetFile.read();
	return dataset.getObjectData();
    }

    //Perform the login
    @Test()
    public void login() {
	Settings settings = new Settings("setting.properties");
	HomePage homePage = new HomePage(Android.driver);
	homePage.signIn(settings.getUserName(), settings.getPassword());
    }
    
    //Search for each item in the TestData
    //Buy the item and verify the item details in checkout
    @Test(dataProvider = "item-to-search")
    public void searchForItem(String searchItem) {
	ProductSearchScreen searchScreen = new ProductSearchScreen(Android.driver);
	searchScreen.searchFor(searchItem);
	searchScreen.selectRandomItem();

	ProductDetailsScreen productDetailsScreen = new ProductDetailsScreen(Android.driver);
	String itemName = productDetailsScreen.getItemName();
	String itemPrice = productDetailsScreen.getItemPrice();
	productDetailsScreen.buy();

	CheckoutScreen checkoutScreen = new CheckoutScreen(Android.driver);
	String itemNameInCheckout = checkoutScreen.getItemName();
	String itemPriceInCheckout = checkoutScreen.getItemPrice();


	checkoutScreen.goToHome();

	assertEquals(itemName, itemNameInCheckout);
	assertEquals(itemPrice, itemPriceInCheckout);

    }
}
