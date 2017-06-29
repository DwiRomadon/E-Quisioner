package helper;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.e_quisioner.R;

import java.util.List;

/**
 * Created by Terminator on 04/06/2017.
 */

public class NewsAdapterAdmin extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<NewsDataAdmin> newsItems;

    public NewsAdapterAdmin(Activity activity, List<NewsDataAdmin> newsItems) {
        this.activity = activity;
        this.newsItems = newsItems;
    }

    @Override
    public int getCount() {
        return newsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return newsItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_news, null);

        TextView no = (TextView) convertView.findViewById(R.id.nomornya);
        TextView id = (TextView) convertView.findViewById(R.id.idnya);
        TextView timestamp = (TextView) convertView.findViewById(R.id.news_timestamp);
        TextView isi = (TextView) convertView.findViewById(R.id.news_isi);

        NewsDataAdmin news = newsItems.get(position);

        no.setText(news.getNomor()+".");
        id.setText(news.getId());
        timestamp.setText(news.getTgl());
        isi.setText(Html.fromHtml(news.getIsi()));

        return convertView;
    }
}