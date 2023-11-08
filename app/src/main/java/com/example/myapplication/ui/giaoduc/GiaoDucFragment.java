package com.example.myapplication.ui.giaoduc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.NewsActivity;
import com.example.myapplication.R;
import com.example.myapplication.XMLDOMParser;
import com.example.myapplication.adapter.NewsAdapter;
import com.example.myapplication.model.News;
import com.example.myapplication.ui.thegioi.TheGioiFragment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GiaoDucFragment extends Fragment {
    ListView lvTieude;
    ArrayList<String> arraylink;

    List<News> hinhList;
    NewsAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_thegioi, container, false);
        arraylink = new ArrayList<>();
        hinhList = new ArrayList<>();
        lvTieude = view.findViewById(R.id.listviewTieude);
        adapter = new NewsAdapter(getContext(), R.layout.lv_new, hinhList);
        lvTieude.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), NewsActivity.class);
                intent.putExtra("linkTintuc", arraylink.get(position));
                startActivity(intent);
            }
        });
        new GiaoDucFragment.ReadRSS().execute("https://vnexpress.net/rss/giao-duc.rss");
        return view;
    }
    private class ReadRSS extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            String tieude = "";

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                tieude = parser.getValue(element, "title");
                // Lấy nội dung CDATA từ thẻ description
                Element descriptionElement = (Element) element.getElementsByTagName("description").item(0);
                String cdataContent = descriptionElement.getTextContent();

                // Sử dụng regex để trích xuất URL của hình ảnh từ nội dung CDATA
                String imageUrl = "";
                Pattern pattern = Pattern.compile("<img\\s+src=\"(.*?)\"");
                Matcher matcher = pattern.matcher(cdataContent);

                if (matcher.find()) {
                    imageUrl = matcher.group(1);
                }

                arraylink.add(parser.getValue(element, "link"));
                if (imageUrl != null) {
                    // Tạo một đối tượng News và thêm vào danh sách
                    News news = new News(imageUrl, tieude);
                    hinhList.add(news);
                }
            }

            adapter.notifyDataSetChanged();
            lvTieude.setAdapter(adapter);

        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content =new StringBuilder();
            try {
                URL url =new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line="";

                while ((line= bufferedReader.readLine())!=null){
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return content.toString();
        }
    }
}