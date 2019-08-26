## git 相关

git fetch 拉取版本库变化

远程库命名 origin

head 指针 指向当前操作的节点

checkout 切换分支

branch  创建新分支/不加名字可以查看当前本地分支，查看远程分支采用branch -a，删除分支用branch -d

merge 分支合并 将其他合并到当前分支上，合并冲突问题 要没有冲突才可以merge，fast-forward模式，会丢失合并前的分支信息，使用--no-ff 禁用fast-forward，并产生一个新的提交commit。可以使用git log 查看提交历史。

git stash 临时保存 list 查看保存的现场  apply：恢复并保留记录，drop删除记录，pop 恢复并删除。

git status 查看工作区

git rebase 把代码建立在别人的基础上