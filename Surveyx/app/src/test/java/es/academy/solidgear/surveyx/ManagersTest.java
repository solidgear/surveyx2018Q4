package es.academy.solidgear.surveyx;


import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Assert;
import org.junit.Test;

import es.academy.solidgear.surveyx.managers.ApiManager;
import es.academy.solidgear.surveyx.managers.DialogsManager;
import es.academy.solidgear.surveyx.managers.NetworkManager;

public class ManagersTest {

    @Test
    public void test_network_manager_is_singleton() {
        NetworkManager networkManager1 = NetworkManager.getInstance();
        NetworkManager networkManager2 = NetworkManager.getInstance();
        Assert.assertTrue(networkManager1 == networkManager2);
    }

    @Test
    public void test_dialog_manager_is_singleton() {
        DialogsManager dialogsManager1 = DialogsManager.getInstance();
        DialogsManager dialogsManager2 = DialogsManager.getInstance();
        Assert.assertTrue(dialogsManager1 == dialogsManager2);
    }

    @Test
    public void test_api_manager_is_prototype() {
        Context context = new MockContext();
        ApiManager apiManager = new ApiManager(context);
        ApiManager apiManager2 = new ApiManager(context);
        Assert.assertTrue(apiManager != apiManager2);
    }
}
