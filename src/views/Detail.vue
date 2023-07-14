<<template>
  <div>
    <el-card style="width: 500px;">
      <el-form label-width="80px" size="small">

        <el-form-item label="小区名">
          <el-input v-model="form.parkingAddress" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="form.status" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="价格（每小时）">
          <el-input v-model="form.priceNow" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="车位主人联系方式">
          <el-input v-model="form.ownerphone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="本周租用次数">
          <el-input v-model="form.weekUseCnt" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="本月租用次数">
          <el-input v-model="form.monthUseCnt" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="本周平均价格">
          <el-input v-model="form.weekAvgPrice" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="本月平均价格">
          <el-input v-model="form.monthAvgPrice" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="大众评分">
          <el-rate
              v-model="form.grades"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}">
          </el-rate>
        </el-form-item>
      </el-form>
      <el-divider content-position="left">车位图片</el-divider>
      <div class="demo-image__placeholder">
        <div class="block">
          <span class="demonstration"></span>
          <el-image v-if="form.parkingUrl" :src="form.parkingUrl"></el-image>
        </div>
      </div>
    </el-card>

    <client-only placeholder="Loading...">
      <comment
          :commentNum="commentNum"
          :commentList="processedCommentList"
          @doSend="handleSend"
          @doChidSend="handleChildSend"
      />
    </client-only>

  </div>
</template>



<script>

import comment from "bright-comment";
import axios from "axios";

export default {
  name: "Detail",
  components:{
    comment
  },
  data(){
    return{
      form:{},
      value:"",
      userInfo: {
        id: 1,
        avatar: ""
      },
      commentNum:0,//评论数
      commentListFromBackend: [], // 从后端获取的评论列表
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }

  },
created() {
  this.getParking().then(res => {
    this.form = res
  })
  this.showComments
},
  mounted() {
    console.log(this.$route)
    this.fetchComments();
  },
  computed: {
    processedCommentList() {
      return this.commentListFromBackend.map(comment => this.processComment(comment));
    },
  },
  methods: {
    processComment(comment) {
      return {
        id: comment.cid,
        commentUser: {
          nickName: comment.nickname,
          avatar: comment.commentAvatar,
        },
        targetUser: comment.targetNickname
            ? {
              nickName: comment.targetNickname,
            }
            : null,
        content: comment.content,
        createDate: this.formatDate(comment.createdAt),
        childrenList: comment.childComments.map(childComment => this.processComment(childComment)),
      };
    },
    formatDate(dateArray) {
      // 格式化日期数组为字符串形式
      const [year, month, day, hour, minute, second] = dateArray;
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
    },

    async getParking() {
      const url = `/parking/${this.$route.query.parkingid}`
      return (await this.request.get(url)).data
    },
    fetchComments() {
      const url = `/comments/${this.$route.query.parkingid}`
      this.request.get(url)
          .then(response => {
            this.commentListFromBackend = response;
            this.commentNum=response.length
          })
          .catch(error => {
            console.error(error);
          });
    },
    handleSend(content) {
      let a = {}
      a.nickname=this.user.nickname;
      a.commentAvatar=this.user.avatarUrl;
      a.targetNickname=null;
      a.content= content;
      a.parkingid=this.$route.query.parkingid;
      a.topid=0;

      // 向后端发送评论数据
     axios.post("http://localhost:9090/comments", a)
          .then(resp => {
            console.log(resp.data);
            this.$message.success("评论成功!");
            console.log('评论发送成功');
          })
          .catch(error => {
            console.error('评论发送失败', error);
            this.$message.error("评论失败，请稍后重试！");
          });

    },
    handleChildSend(content, bid, pid) {
      // 执行评论列表中文本框发送事件的逻辑
      console.log('回复评论:', content);
      console.log('被评论用户id:', bid);
      console.log('父级评论id:', pid);
      let a = {}
      a.nickname=this.user.nickname;
      a.commentAvatar=this.user.avatarUrl;
      a.targetNickname="瑶宝";
      a.content= content;
      a.parkingid=this.$route.query.parkingid;
      a.parentId=pid;
      // 向后端发送评论数据
      axios.post("http://localhost:9090/comments", a)
          .then(resp => {
            console.log(resp.data);
            this.$message.success("回复评论成功!");
            console.log('评论发送成功');
          })
          .catch(error => {
            console.error('评论发送失败', error);
            this.$message.error("回复评论失败，请稍后重试！");
          });


    },


  }

}
</script>

<style scoped>

</style>