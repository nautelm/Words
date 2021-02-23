package com.example.wordsapp

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsapp.databinding.FragmentLetterListBinding


/*
 * fragmentのライフサイクル
 * onCreate()
 * onCreateView()
 * onViewCreated()
 * onStart()
 * onResume()
 * onPause()
 * onStop()
 * onDestroyView()
 * onDestroy()
 */
class LetterListFragment : Fragment() {

    /*
     * viewBindingを用いる
     * FragmentのライフサイクルはFragmentのViewのライフサイクルより長いため,
     * Fragment自体がBindingの参照を保持するとリークしてしまう
     * ゆえにonDestroy()でbindingの参照を解放する
     */
    private var _binding: FragmentLetterListBinding? = null
    // onCreateViewとonDestroyViewの間だけ有効なプロパティ
    // get() : getOnly つまり値を取得することはできるが、
    // 一度割り当てられると他のなにかに割り当てられることはできない
    private val binding get() = _binding!!
    // recyclerViewのインスタンス。後で初期化
    private lateinit var recyclerView: RecyclerView
    // LinearLayoutを使っているか
    private var isLinearLayoutManager = true


    /*
     * onCreate() : フラグメントの作成時に呼び出される

     * savedInstanceState : フラグメントが復元された時(回転時とか)に用いるフラグメントの状態を保持したBundle
     * setHasOptionsMenu : メニューを表示するためにtrueにする。あとでsetCreateOptionsMenu()を実装
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    /*
     * onCreateView() : フラグメントが初めてUIを描画するタイミングで呼び出される
     * フラグメントのレイアウトのルートとなっているViewを返してUIを描画(nullなら何も描画しない)

     * inflater : フラグメント内のViewを生成するために使用するLayoutInflaterオブジェクト
     * container : フラグメントの親となるViewGroup
     * savedInstanceState : 省略

     * inflateメソッド : レイアウトXMLからViewを作成する
     * (resource: Int, root:ViewGroup!, attachToRoot: Boolean)
     * resource : 適用するレイアウトXMLのリソースID
     * root : 作成するViewをattachするViewを指定 = ViewGroupのこと
     * attachToRoot : falseならViewを返す

     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // 生成されたBindingクラスに含まれる静的inflate()メソッドを使う
        _binding = FragmentLetterListBinding.inflate(inflater, container, false)
        // rootViewへの参照
        val view = binding.root

        return view
    }

    /*
     * onViewCreated() : Viewの初期化を行う
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // recyclerViewの初期化
        recyclerView = binding.recyclerView
        // LinearLayoutかGridLayoutのどちらかを適用する
        chooseLayout()
    }

    /*
     * onDestroyView() : Viewが終了するとき
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // bindingへの参照を解放する
        _binding = null
    }

    /*
     * onCreateOptionsMenu : メニューを作成して表示する
     * menu : Menuクラスのインスタンス
     * inflater : MenuのレイアウトXMLからViewを作成する
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)

        // Menuのアイテムへの処理
        // LinearLayoutかGridLayoutを選択するためのボタン
        val layoutButton = menu.findItem(R.id.action_switch_layout)
        // ボタンのレイアウトを変更
        setIcon(layoutButton)
    }

    // Layoutを変更する(LinearLayout or GridLayout)
    // isLinearLayoutManagerで条件分岐
    // layoutManagerとadapterの設定
    private fun chooseLayout() {
        when (isLinearLayoutManager) {
            true -> {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = LetterAdapter()
            }
            false -> {
                recyclerView.layoutManager = GridLayoutManager(context, 4)
                recyclerView.adapter = LetterAdapter()
            }
        }
    }

    // レイアウト変更ボタンのアイコンの設定
    private fun setIcon(menuItem: MenuItem?) {
        // nullが渡されたらなにもしない
        if (menuItem == null)
            return

        menuItem.icon =
                if (isLinearLayoutManager)
                    ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
                else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
    }

    // ボタンが押された時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                isLinearLayoutManager = !isLinearLayoutManager
                chooseLayout()
                setIcon(item)

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}