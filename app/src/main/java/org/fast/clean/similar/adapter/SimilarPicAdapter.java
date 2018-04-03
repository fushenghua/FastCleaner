package org.fast.clean.similar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.fast.clean.R;
import org.fast.clean.similar.SimilarPhotoActivity;
import org.fast.clean.similar.bean.GroupData;
import org.fast.clean.similar.bean.PhotoData;
import org.fast.clean.view.circleprogress.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by silver on 17-3-2.
 */

public class SimilarPicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int mSize;

    public static final int VIEW_TYPE_SECTION = 0;

    public static final int VIEW_TYPE_ITEM = 1;

    private int mItemCount;

    public List<GroupData> mGroupData = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    private Context mContext;

    public SimilarPicAdapter(Context context) {
        this.mContext = context;
        mSize = Utils.getScreenWidth(context) / SimilarPhotoActivity.GALLERY_COLUMM_COUNT;
    }

    public void setGroupDatas(List<GroupData> groupData) {
        for (GroupData group : groupData) {
            group.title = Utils.createSectionTime(mContext, group.getPhotos().get(0).lastModified * 1000);
        }
        this.mGroupData = groupData;
        updateItemCount();
    }

    private void updateItemCount() {
        int count = 0;
        for (GroupData group : mGroupData) {
            count += group.getPhotos().size() + 1;
        }
        mItemCount = count;
    }

    public int getGroupCount() {
        return mGroupData.size();
    }

    public List<GroupData> getGroupData() {
        return mGroupData;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<PhotoData> getSelectedList() {
        List<PhotoData> tmpList = new ArrayList<>();
        for (GroupData group : mGroupData) {
            tmpList.addAll(group.getCheckList());
        }
        return tmpList;
    }

    public void unSelectAll() {
        for (GroupData group : mGroupData) {
//            group.unSelectAll();
        }
        notifyDataSetChanged();
    }

    public void smartSelect() {
        for (GroupData group : mGroupData) {
//            group.smartSelelct();
        }
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder holder;
        switch (viewType) {
            case VIEW_TYPE_SECTION:
                View header = LayoutInflater.from(parent.getContext()).inflate(R.layout.similar_header_item, parent, false);
                holder = new SectionHolder(header);
                break;
            case VIEW_TYPE_ITEM:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item_similar_layout, parent, false);
                holder = new ItemHolder(itemView, onItemClickListener);
                ViewGroup.LayoutParams params = itemView.getLayoutParams();
                params.height = mSize;
                params.width = mSize;
                itemView.setLayoutParams(params);
                break;
            default:
                holder = null;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(holder);
        int itemCount = 0;
        int groupPosition = 0;
        int childPosition = 0;
        int childCount = 0;
        for (GroupData g : mGroupData) {
            if (position == itemCount) {
                bindSectionHolder((SectionHolder) holder, groupPosition);
                return;
            }
            itemCount++;
            childPosition = position - itemCount;
            childCount = g.getPhotos().size();
            if (childPosition < childCount) {
                bindItemHolder((ItemHolder) holder, groupPosition, childPosition);
                return;
            }
            itemCount += childCount;
            groupPosition++;
        }
    }

    /**
     * Delete item in group
     *
     * @param groupPosition
     * @param childPosition
     */
    public void removeChild(int groupPosition, int childPosition) {
        int index = getChildPositionForPosition(groupPosition, childPosition);
        if (index >= 0) {
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount() - index);
        }
    }

    /*** 根据File删除一组里的某个子项，如果一组子孩子小于等于1，则移除组*/
    public void removeItem(PhotoData photoFile) {
        if (mGroupData != null && !mGroupData.isEmpty()) {
            int i = 0;
            int position = 0;
            int subItemCount = 0;
            for (GroupData group : mGroupData) {
                int index = group.getPhotos().indexOf(photoFile);
                int itemCount = countGroupItem(i);
                subItemCount += itemCount;
                if (index != -1) {
                    position = getChildPositionForIndex(i, index);
                    notifyItemRemoved(position);
                    group.getPhotos().remove(index);
                    subItemCount--;
                    itemCount--;
                    if (group.getPhotos().size() <= 1) {

//                        notifyDataSetChanged();
                        mGroupData.remove(group);
                        notifyItemRangeRemoved(subItemCount - itemCount, itemCount);
                    }
                    break;
                }
                i++;
            }
            updateItemCount();
        }
    }


    public int getGroupPositionForPosition(int position) {
        int count = 0;
        int groupCount = mGroupData.size();
        for (int i = 0; i < groupCount; i++) {
            count += countGroupItem(i);
            if (position < count) {
                return i;
            }
        }
        return -1;
    }

    public PhotoData getChildForPosition(int position) {
        int groupPosition = getGroupPositionForPosition(position);
        if (groupPosition == -1) return null;
        int childPosition = getChildPositionForPosition(groupPosition, position);
        if (childPosition == -1) return null;
        return mGroupData.get(groupPosition).getPhotos().get(childPosition);
    }

    public int getChildPositionForPosition(int position) {
        int groupPosition = getGroupPositionForPosition(position);
        int childPosition = getChildPositionForPosition(groupPosition, position);
        return childPosition;
    }

    /**
     * 根据recyleView下标计算position在组中位置（childPosition）
     *
     * @param groupPosition 所在的组
     * @param position      下标
     * @return 子项下标 childPosition
     */
    public int getChildPositionForPosition(int groupPosition, int position) {
        if (groupPosition < mGroupData.size()) {
            int itemCount = countGroupRangeItem(0, groupPosition + 1);
            GroupData groupData = mGroupData.get(groupPosition);
            int p = groupData.getPhotos().size() - (itemCount - position);
            if (p >= 0) {
                return p;
            }
        }
        return -1;
    }

    /**
     * 根据index下标计算position在recyleView中位置（position）
     *
     * @param groupPosition 所在的组
     * @param index         下标
     * @return 子项下标 childPosition
     */
    public int getChildPositionForIndex(int groupPosition, int index) {
        if (groupPosition < mGroupData.size()) {
            int itemCount = countGroupRangeItem(0, groupPosition + 1);
            GroupData groupData = mGroupData.get(groupPosition);
            int p = (itemCount - groupData.getPhotos().size()) + index;
            if (p >= 0) {
                return p;
            }
        }
        return -1;
    }

    /**
     * 根据Photo计算position在recyleView中位置（position）
     *
     * @param photoFile
     * @return
     */
    public int getChildPositionForObject(PhotoData photoFile) {
        if (mGroupData != null && !mGroupData.isEmpty()) {
            int i = 0;
            int position = 0;
            for (GroupData group : mGroupData) {
                int index = group.getPhotos().indexOf(photoFile);
                if (index != -1) {
                    position = getChildPositionForIndex(i, index);
                    return position;
                }
                i++;
            }
        }
        return -1;
    }

    /**
     * 计算多个Group的Item的总和
     *
     * @return
     */
    public int countGroupRangeItem(int start, int count) {
        int itemCount = 0;
        int size = mGroupData.size();
        for (int i = start; i < size && i < start + count; i++) {
            itemCount += countGroupItem(i);
        }
        return itemCount;
    }

    /**
     * 计算一个Group里有多少个Item+title
     *
     * @param groupPosition
     * @return
     */
    public int countGroupItem(int groupPosition) {
        int itemCount = 0;
        if (groupPosition < mGroupData.size()) {
            GroupData group = mGroupData.get(groupPosition);
            itemCount += group.getPhotos().size() + 1;
        }
        return itemCount;
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public int getItemViewType(int position) {
//        if (mGroupData.get(position) instanceof GroupData) {
//            return VIEW_TYPE_SECTION;
//        } else {
//            return VIEW_TYPE_ITEM;
//        }
        return getItemType(position);
    }

    void bindSectionHolder(SectionHolder sectionHolder, int groupPosition) {
        TextView sectionItem = sectionHolder.mContent;
        GroupData groupData = mGroupData.get(groupPosition);
        sectionItem.setText(groupData.title);
    }

    void bindItemHolder(ItemHolder itemHolder, int groupPosition, int childPosition) {
        PhotoData photoFile = mGroupData.get(groupPosition).getPhotos().get(childPosition);
        itemHolder.setSelected(photoFile.isSelected);
        Glide.with(mContext).load(new File(photoFile.filePath)).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().thumbnail(0.1f).into(itemHolder.mContent);
    }

    public int getItemType(final int position) {
        int count = 0;
        for (GroupData g : mGroupData) {
            if (position == count) {
                return VIEW_TYPE_SECTION;
            }
            count += 1;
            if (position == count) {
                return VIEW_TYPE_ITEM;
            }
            count += g.getPhotos().size();
            if (position < count) {
                return VIEW_TYPE_ITEM;
            }
        }
        throw new IllegalStateException("Could not find item getItemType for item position " + position);
    }

    /**
     * toggle select?
     */
    public void toggleSelect(int position) {
        if (mGroupData != null && !mGroupData.isEmpty()) {
            PhotoData photoFile = getChildForPosition(position);
            if (photoFile != null) {
                photoFile.isSelected = !photoFile.isSelected;
                notifyItemChanged(position);
            }
        }
    }

    public void notifyItemDataChanged(PhotoData photoFile) {
        int position = getChildPositionForObject(photoFile);
        if (position != -1) {
            notifyItemChanged(position);
        }
    }


    protected class SectionHolder extends RecyclerView.ViewHolder {
        public TextView mContent;

        public SectionHolder(View view) {
            super(view);
            mContent = (TextView) view.findViewById(R.id.tv_date);
        }
    }


    protected class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mContent, mItemSelected;
        private OnItemClickListener onItemClickListener;
        private TextView mItem_excellent;

        public ItemHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            mContent = (ImageView) view.findViewById(R.id.content);
            mItemSelected = (ImageView) view.findViewById(R.id.item_selected);
            mItemSelected.setOnClickListener(this);
            view.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null && getAdapterPosition() != -1) {
                if (v.getId() == R.id.item_selected) {
                    onItemClickListener.onItemSelected(v, getAdapterPosition());
                } else {
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        }

        public void setSelected(boolean selected) {
            mItemSelected.setSelected(selected);
            mItemSelected.setVisibility(View.VISIBLE);
        }

        public void setFined(boolean fined) {
            mItem_excellent.setVisibility(fined ? View.VISIBLE : View.GONE);
        }

    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * AdapterView has been clicked.
     */
    public interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param view     The view within the AdapterView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         */
        void onItemClick(View view, int position);

        void onItemSelected(View view, int position);
    }

}
