import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import first from '@/components/views/first'
import seckill_list from '@/components/views/seckill_list'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },{
      path: '/first',
      name: 'first',
      component: first
    },{
      path: '/seckill_list',
      name: 'seckill_list',
      component: seckill_list
    }
  ]
})
