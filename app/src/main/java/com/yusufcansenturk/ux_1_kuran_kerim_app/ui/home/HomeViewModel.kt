package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerList.Data
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.hadis.Hadis
import com.yusufcansenturk.ux_1_kuran_kerim_app.repository.SureRepository
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.Constants
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.Constants.COLLECTION_FAVORITE
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SureRepository
) : ViewModel(){

    val sureList = MutableLiveData<List<Data>>()
    val errorMessage = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()

    private val hadisler: MutableList<Hadis> = mutableListOf()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val email = auth.currentUser!!.email

    private var initialSureList = listOf<Data>()
    private var isSearchStarting = true


    init {
        loadSure()
        initHadislerList()
    }


    fun searchSureList(query: String) {
        val listToSearch = if (isSearchStarting) {
            sureList.value ?: return
        } else {
            initialSureList
        } ?: return

        viewModelScope.launch(Dispatchers.Main) {
            if (query.isEmpty()){
                sureList.value = initialSureList
                isSearchStarting = true
                return@launch
            }

            val results = listToSearch!!.filter {
                it.name!!.contains(query.trim(), ignoreCase = true)
            }

            if (isSearchStarting) {
                initialSureList = sureList.value!!
                isSearchStarting = false
            }

            sureList.value = results

        }

    }

    fun addFavorite(hashMap: HashMap<String, Any>, sureId: String) {
        val userFavoritesCollection = db.collection(COLLECTION_FAVORITE).document(email.toString()).collection("verses")
        userFavoritesCollection.document(sureId)
            .set(hashMap)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun removeFavorite(sureId: String) {
        val itemsRef = db.collection(COLLECTION_FAVORITE).document(email.toString()).collection("verses")
        val query = itemsRef.document(sureId).delete()
            query
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
    }




    fun loadSure() {
        viewModelScope.launch {

            isLoading.value = true

            when(val result = repository.getSureList()) {
                is Resource.Success -> {

                    val sureItems = result.data!!.data!!.mapIndexed { index, data ->
                        Data(
                            data?.id,
                            data?.name,
                            data?.nameOriginal,
                            data?.pageNumber,
                            data?.slug,
                            data?.verseCount
                        )
                    } as List<Data>

                    println("Success Resource")
                    isLoading.value = false
                    errorMessage.value = false
                    sureList.value = sureItems
                }

                is Resource.Loading -> {
                    errorMessage.value = false
                    isLoading.value = false
                    println("Loading Resource")
                }

                is Resource.Error -> {
                    errorMessage.value = true
                    isLoading.value = false
                    println("Error Resource")
                }


                else -> {}
            }

        }

    }

    fun getRandomHadis() : Hadis {
        return  hadisler.random()
    }

    fun initHadislerList() {
        hadisler.add(Hadis(1, "Dua, ibadetin ??z??d??r.", "Hz muhammed"))
        hadisler.add(Hadis(2, "M??min kimseye lanet etmek, onu ??ld??rmek gibidir. Her kim, m??min bir kimseyi k??f??rle itham ederse, onu ??ld??rm???? gibidir. Kim kendisini herhangi bir ??eyle keserse, k??yamet g??n??nde, o ??eyle kesilir.", "Hz Muhammed"))
        hadisler.add(Hadis(3, "Namazda Fatiha suresini okumayan kimsenin namaz?? yoktur.", "Hz muhammed"))
        hadisler.add(Hadis(4, "G??ne?? ve ay; bir kimsenin ??l??m?? ya da hayat?? i??in tutulmazlar. Fakat g??ne?? ve ay, Allah'??n varl??????n??n delillerindendir. Allah, bunlar?? kullar??na g??sterir. G??ne?? ve ay??n tutulduklar??n?? g??rd??????n??z zaman hemen namaz k??lmayan ko??un.", "Hz Muhammed"))
        hadisler.add(Hadis(5, "Kim hakk?? olmad?????? halde ba??kas??na ait bir toprak par??as??n?? gasp ederse k??yamet g??n?? o yerin yedi kat topra???? o ki??inin s??rt??na y??klenir .", "Hz Muhammed"))
        hadisler.add(Hadis(6, "Sizden birinin, bir lokmas?? d????t??????nde onu als??n, temizleyip yesin, ??eytana b??rakmas??n.", "Hz Muhammed"))
        hadisler.add(Hadis(7, "K??????????m??ze merhamet etmeyen ve b??y??????m??z??n ??erefini tan??mayan bizden de??ildir.", "Hz Muhammed"))
        hadisler.add(Hadis(8, "Bir adam Allah r??zas?? i??in ailesinin nafakas??n?? temin ederse, onun i??in sadaka olur.", "Hz Muhammed"))
        hadisler.add(Hadis(9,  "??yilik g??zel ahlakt??r; g??nah da i??inde teredd??t uyand??ran ve halk??n bilmesini istemedi??in ??eydir.", "Hz muhammed"))
        hadisler.add(Hadis(10, "Sadaka mal?? eksiltmez. Bir kul ba??kalar??n?? affederse Allah onun ??erefini art??r??r. Kim Allah i??in tevazu g??sterirse Allah onu y??kseltir.", "Hz Muhammed"))
        hadisler.add(Hadis(11,  "D??nyada garip yahut yolcu ol.", "Hz Muhammed"))
        hadisler.add(Hadis(12, "Zul??mden sak??n??n; zira zul??m k??yamet g??n??nde z??lumat(karanl??k)t??r. Cimrilikten sak??n??n; zira cimrilik sizden ??ncekileri helak etti; onlar?? kanlar??n?? d??kmeye ve haramlar?? helal saymaya sevk etti.", "Hz Muhammed"))
        hadisler.add(Hadis(13,  "G??c??n??z??n yetti??i ibadeti yap??n. Zira siz usanmad??k??a Allah usanmaz.", "Hz muhammed"))
        hadisler.add(Hadis(14, "Allah yemek yeyip de arkas??ndan kendisine hamdeden, su i??ip de arkas??ndan kendisine hamdeden kulundan raz?? olur." , "Hz Muhammed"))
        hadisler.add(Hadis(15, "Her kul ??ld?????? hal ??zere dirilir.", "Hz Muhammed"))
        hadisler.add(Hadis(16,  "??l??y?? ???? ??ey takip eder: Ailesi, mal?? ve ameli. ??kisi geri d??ner; biri kal??r: Ailesi ve mal?? geri d??ner; ameli ise kal??r." , "Hz Muhammed"))
        hadisler.add(Hadis(17, "Kim bir ??eye yemin eder de takvaya daha yak??n ba??ka bir ??ey g??r??rse, o ??eyi yaps??n.", "Hz Muhammed"))
        hadisler.add(Hadis(18, "Allah da k??skan??r. Allah???n k??skanmas?? haramlar??n ??i??nenmesidir.", "Hz Muhammed"))
        hadisler.add(Hadis(19, "Al????veri?? yapanlar meclisten ayr??lmad??klar?? s??rece muhayyerdirler. E??er do??ru konu??urlarsa al????veri??lerinin bereketini g??r??rler. E??er baz?? ??eyleri saklarlarsa al????veri??lerinin bereketi ka??ar." , "Hz Muhammed"))
        hadisler.add(Hadis(20, "Allah kimin hayr??n?? murad ederse, ona musibet verir." , "Hz Muhammed"))
        hadisler.add(Hadis(21, "Taharet iman??n yar??s??d??r. Elhamd??lillah mizan?? doldurur, s??bhanallah velhamdulillah g??klerle yerin aras??n?? doldururlar. Namaz nurdur, sadaka delildir, sab??r ??????kt??r, Kur'??n da ya lehine ya da aleyhine ??ahittir. Herkes sabahleyin kalkar; nefsini satar; ya onu azat eder, yahut da helak eder.", "Hz muhammed"))
        hadisler.add(Hadis(22, "Allah; g??nd??z g??nah i??leyenin tevbe etmesi i??in gece elini a??ar ve gece g??nah i??leyenin tevbe etmesi i??in de g??nd??z elini a??ar. Bu, g??ne??in bat??dan do??mas??na kadar devam eder.", "Hz muhammed"))
        hadisler.add(Hadis(23, "Pehlivan ba??kalar??n?? yere ??alan (yenen) de??ildir. As??l pehlivan ??fke an??nda nefsine sahip oland??r." , "Hz Muhammed"))
        hadisler.add(Hadis(24, "Haks??z yere ??ld??r??len her canda ??dem'in ilk o??luna bir g??nah pay?? vard??r. ????nk?? ??ld??rme olay??n?? ilk ??nce o ??det etmi??tir.", "Hz muhammed"))
        hadisler.add(Hadis(25, "Kim bir hayra delalet ederse, onu yapan kadar sevap kazan??r.", "Hz Muhammed"))
        hadisler.add(Hadis(26, "Kim Allah yolunda bir gaziyi te??hiz ederse gaza etmi?? olur ve kim bir gazinin ailesine bakarsa, yine gaza etmi?? olur.", "Hz muhammed"))
        hadisler.add(Hadis(27, "M??naf??????n al??meti ????t??r: Konu??tu??u zaman yalan s??yler, s??z verdi??i zaman ??zerinde durmaz, bir ??ey emanet edilirse h??yanet eder.", "Hz muhammed"))
        hadisler.add(Hadis(28,  "Kim iki k??z ??ocu??una bulu??a erinceye kadar bakarsa ben ve o cennette ????yleyiz. (??ki elini birbirine birle??tirmi??tir)", "Hz muhammed"))
        hadisler.add(Hadis(29, "Kim Allah r??zas?? i??in ????renilmesi gereken bir ilmi d??nya meta?? elde etmek i??in ????renirse, k??yamet g??n??nde cennetin kokusunu bile duymaz.", "Hz muhammed"))
        hadisler.add(Hadis(30, "Yedi tehlikeli g??nahtan sak??n??n. Allah'a ??irk ko??mak, b??y?? yapmak, Allah'??n haram k??ld?????? cana haks??z yere k??ymak, faiz yemek, yetim mal?? yemek, sava??tan ka??mak ve namuslu, bir ??eyden haberi olmayan M??'min kad??nlara iftira etmek.", "Hz Muhammed"))
        hadisler.add(Hadis(31, "Zenginin borcunu savsaklamas?? zul??md??r. Biriniz zengine havale edilirse, kabul etsin." , "Hz muhammed"))
        hadisler.add(Hadis(32, "???? ki??i olursan??z ??tekisi olmadan iki ki??i insanlara kar????madan gizli konu??mas??nlar. ????nk?? bu onu ??zer.", "Hz muhammed"))
        hadisler.add(Hadis(33,  "Bir m??'mine m??'min karde??ini ???? g??nden fazla terk etmesi helal de??ildir. E??er ???? g??n ge??erse, kar????s??na ????ks??n, ona selam versin. E??er selam??n?? al??rsa, sevapta ortak olurlar. E??er almazsa, g??nah?? o y??klenir ve selam veren k??sme durumundan ????kar.", "Hz Muhammed"))
        hadisler.add(Hadis(34, "Hasetten uzak durun; zira ate?? nas??l odunu/ kuru otu yakar bitirirse, haset de iyilikleri ??ylece yakar bitirir." , "Hz muhammed"))
        hadisler.add(Hadis(35, "??l??lere s??vmeyin; ????nk?? onlar amelleri ile ba?? ba??a kalm????lard??r.", "Hz Muhammed"))
        hadisler.add(Hadis(36, "Lanet edenler k??yamet g??n??nde ne ??efaat??i ne de ??ahid olabilirler.", "Hz Muhammed"))
        hadisler.add(Hadis(37, "??nsanlar??n aras??n?? bulan; hay??r ula??t??ran yahut hay??r s??yleyen yalanc?? de??ildir.", "Hz muhammed"))
        hadisler.add(Hadis(38,  "Yalan??n en b??y?????? g??rmedi??i halde r??ya g??rd??m, demektir.", "Hz Muhammed"))
        hadisler.add(Hadis(39, "Laf g??t??r??p getiren cennete girmez.", "Hz Muhammed"))
        hadisler.add(Hadis(40,  "Kim bir M??sl??man karde??inin namusunu m??dafaa ederse, Allah da k??yamet g??n??nde onun y??z??n?? cehennemden korur.", "Hz Muhammed"))
    }

}