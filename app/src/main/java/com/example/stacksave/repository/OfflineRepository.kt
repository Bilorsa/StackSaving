package com.example.stacksave.repository
import android.content.Context
import com.example.stacksave.model.StoreDeal
import org.json.JSONArray
import org.json.JSONObject
class OfflineRepository(context: Context) {
    private val prefs = context.getSharedPreferences("stacksave_offline", Context.MODE_PRIVATE)
    fun saveDeals(deals: List<StoreDeal>) { val a=JSONArray(); deals.forEach { d -> a.put(JSONObject().apply { put("id",d.id);put("store",d.storeName);put("name",d.productName);put("original",d.originalPrice);put("sale",d.salePrice);put("cashback",d.cashbackRate);put("category",d.category) }) }; prefs.edit().putString("deals",a.toString()).apply() }
    fun loadDeals(): List<StoreDeal> { val raw=prefs.getString("deals",null) ?: return emptyList(); val a=JSONArray(raw); return buildList { for(i in 0 until a.length()){ val o=a.getJSONObject(i); add(StoreDeal(o.getString("id"),o.getString("store"),o.getString("name"),o.getDouble("original"),o.getDouble("sale"),o.getDouble("cashback"),o.getString("category"))) } } }
    fun setOfflineEnabled(enabled:Boolean)=prefs.edit().putBoolean("offline",enabled).apply()
}
