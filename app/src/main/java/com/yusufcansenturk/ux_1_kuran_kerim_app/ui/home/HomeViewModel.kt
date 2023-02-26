package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerList.Data
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.hadis.Hadis
import com.yusufcansenturk.ux_1_kuran_kerim_app.repository.SureRepository
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
        hadisler.add(Hadis(1, "Dua, ibadetin özüdür.", "Hz muhammed"))
        hadisler.add(Hadis(2, "Mümin kimseye lanet etmek, onu öldürmek gibidir. Her kim, mümin bir kimseyi küfürle itham ederse, onu öldürmüş gibidir. Kim kendisini herhangi bir şeyle keserse, kıyamet gününde, o şeyle kesilir.", "Hz Muhammed"))
        hadisler.add(Hadis(3, "Namazda Fatiha suresini okumayan kimsenin namazı yoktur.", "Hz muhammed"))
        hadisler.add(Hadis(4, "Güneş ve ay; bir kimsenin ölümü ya da hayatı için tutulmazlar. Fakat güneş ve ay, Allah'ın varlığının delillerindendir. Allah, bunları kullarına gösterir. Güneş ve ayın tutulduklarını gördüğünüz zaman hemen namaz kılmayan koşun.", "Hz Muhammed"))
        hadisler.add(Hadis(5, "Kim hakkı olmadığı halde başkasına ait bir toprak parçasını gasp ederse kıyamet günü o yerin yedi kat toprağı o kişinin sırtına yüklenir .", "Hz Muhammed"))
        hadisler.add(Hadis(6, "Sizden birinin, bir lokması düştüğünde onu alsın, temizleyip yesin, şeytana bırakmasın.", "Hz Muhammed"))
        hadisler.add(Hadis(7, "Küçüğümüze merhamet etmeyen ve büyüğümüzün şerefini tanımayan bizden değildir.", "Hz Muhammed"))
        hadisler.add(Hadis(8, "Bir adam Allah rızası için ailesinin nafakasını temin ederse, onun için sadaka olur.", "Hz Muhammed"))
        hadisler.add(Hadis(9,  "İyilik güzel ahlaktır; günah da içinde tereddüt uyandıran ve halkın bilmesini istemediğin şeydir.", "Hz muhammed"))
        hadisler.add(Hadis(10, "Sadaka malı eksiltmez. Bir kul başkalarını affederse Allah onun şerefini artırır. Kim Allah için tevazu gösterirse Allah onu yükseltir.", "Hz Muhammed"))
        hadisler.add(Hadis(11,  "Dünyada garip yahut yolcu ol.", "Hz Muhammed"))
        hadisler.add(Hadis(12, "Zulümden sakının; zira zulüm kıyamet gününde zülumat(karanlık)tır. Cimrilikten sakının; zira cimrilik sizden öncekileri helak etti; onları kanlarını dökmeye ve haramları helal saymaya sevk etti.", "Hz Muhammed"))
        hadisler.add(Hadis(13,  "Gücünüzün yettiği ibadeti yapın. Zira siz usanmadıkça Allah usanmaz.", "Hz muhammed"))
        hadisler.add(Hadis(14, "Allah yemek yeyip de arkasından kendisine hamdeden, su içip de arkasından kendisine hamdeden kulundan razı olur." , "Hz Muhammed"))
        hadisler.add(Hadis(15, "Her kul öldüğü hal üzere dirilir.", "Hz Muhammed"))
        hadisler.add(Hadis(16,  "Ölüyü üç şey takip eder: Ailesi, malı ve ameli. İkisi geri döner; biri kalır: Ailesi ve malı geri döner; ameli ise kalır." , "Hz Muhammed"))
        hadisler.add(Hadis(17, "Kim bir şeye yemin eder de takvaya daha yakın başka bir şey görürse, o şeyi yapsın.", "Hz Muhammed"))
        hadisler.add(Hadis(18, "Allah da kıskanır. Allah?ın kıskanması haramların çiğnenmesidir.", "Hz Muhammed"))
        hadisler.add(Hadis(19, "Alışveriş yapanlar meclisten ayrılmadıkları sürece muhayyerdirler. Eğer doğru konuşurlarsa alışverişlerinin bereketini görürler. Eğer bazı şeyleri saklarlarsa alışverişlerinin bereketi kaçar." , "Hz Muhammed"))
        hadisler.add(Hadis(20, "Allah kimin hayrını murad ederse, ona musibet verir." , "Hz Muhammed"))
        hadisler.add(Hadis(21, "Taharet imanın yarısıdır. Elhamdülillah mizanı doldurur, sübhanallah velhamdulillah göklerle yerin arasını doldururlar. Namaz nurdur, sadaka delildir, sabır ışıktır, Kur'ân da ya lehine ya da aleyhine şahittir. Herkes sabahleyin kalkar; nefsini satar; ya onu azat eder, yahut da helak eder.", "Hz muhammed"))
        hadisler.add(Hadis(22, "Allah; gündüz günah işleyenin tevbe etmesi için gece elini açar ve gece günah işleyenin tevbe etmesi için de gündüz elini açar. Bu, güneşin batıdan doğmasına kadar devam eder.", "Hz muhammed"))
        hadisler.add(Hadis(23, "Pehlivan başkalarını yere çalan (yenen) değildir. Asıl pehlivan öfke anında nefsine sahip olandır." , "Hz Muhammed"))
        hadisler.add(Hadis(24, "Haksız yere öldürülen her canda Âdem'in ilk oğluna bir günah payı vardır. Çünkü öldürme olayını ilk önce o âdet etmiştir.", "Hz muhammed"))
        hadisler.add(Hadis(25, "Kim bir hayra delalet ederse, onu yapan kadar sevap kazanır.", "Hz Muhammed"))
        hadisler.add(Hadis(26, "Kim Allah yolunda bir gaziyi teçhiz ederse gaza etmiş olur ve kim bir gazinin ailesine bakarsa, yine gaza etmiş olur.", "Hz muhammed"))
        hadisler.add(Hadis(27, "Münafığın alâmeti üçtür: Konuştuğu zaman yalan söyler, söz verdiği zaman üzerinde durmaz, bir şey emanet edilirse hıyanet eder.", "Hz muhammed"))
        hadisler.add(Hadis(28,  "Kim iki kız çocuğuna buluğa erinceye kadar bakarsa ben ve o cennette şöyleyiz. (İki elini birbirine birleştirmiştir)", "Hz muhammed"))
        hadisler.add(Hadis(29, "Kim Allah rızası için öğrenilmesi gereken bir ilmi dünya metaı elde etmek için öğrenirse, kıyamet gününde cennetin kokusunu bile duymaz.", "Hz muhammed"))
        hadisler.add(Hadis(30, "Yedi tehlikeli günahtan sakının. Allah'a şirk koşmak, büyü yapmak, Allah'ın haram kıldığı cana haksız yere kıymak, faiz yemek, yetim malı yemek, savaştan kaçmak ve namuslu, bir şeyden haberi olmayan Mü'min kadınlara iftira etmek.", "Hz Muhammed"))
        hadisler.add(Hadis(31, "Zenginin borcunu savsaklaması zulümdür. Biriniz zengine havale edilirse, kabul etsin." , "Hz muhammed"))
        hadisler.add(Hadis(32, "Üç kişi olursanız ötekisi olmadan iki kişi insanlara karışmadan gizli konuşmasınlar. Çünkü bu onu üzer.", "Hz muhammed"))
        hadisler.add(Hadis(33,  "Bir mü'mine mü'min kardeşini üç günden fazla terk etmesi helal değildir. Eğer üç gün geçerse, karşısına çıksın, ona selam versin. Eğer selamını alırsa, sevapta ortak olurlar. Eğer almazsa, günahı o yüklenir ve selam veren küsme durumundan çıkar.", "Hz Muhammed"))
        hadisler.add(Hadis(34, "Hasetten uzak durun; zira ateş nasıl odunu/ kuru otu yakar bitirirse, haset de iyilikleri öylece yakar bitirir." , "Hz muhammed"))
        hadisler.add(Hadis(35, "Ölülere sövmeyin; çünkü onlar amelleri ile baş başa kalmışlardır.", "Hz Muhammed"))
        hadisler.add(Hadis(36, "Lanet edenler kıyamet gününde ne şefaatçi ne de şahid olabilirler.", "Hz Muhammed"))
        hadisler.add(Hadis(37, "İnsanların arasını bulan; hayır ulaştıran yahut hayır söyleyen yalancı değildir.", "Hz muhammed"))
        hadisler.add(Hadis(38,  "Yalanın en büyüğü görmediği halde rüya gördüm, demektir.", "Hz Muhammed"))
        hadisler.add(Hadis(39, "Laf götürüp getiren cennete girmez.", "Hz Muhammed"))
        hadisler.add(Hadis(40,  "Kim bir Müslüman kardeşinin namusunu müdafaa ederse, Allah da kıyamet gününde onun yüzünü cehennemden korur.", "Hz Muhammed"))
    }

}