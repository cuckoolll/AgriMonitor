layui.use(['form','layer','util','table','laydate'], function(form,layer,util,table,laydate) {
	var curyear=util.toDateString(new Date(), 'yyyy');
	var curmonth=util.toDateString(new Date(), 'yyyyMM');
	var months=['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'];
	var chart1, chart2, chart3, chart4, chart5;
	var option1,option2,option3,option4,option5;
	
	var yeardata;
	laydate.render({
	    elem: '#year',
	    type: 'year',
	    value:curyear,
	    done: function(value, date, endDate){
	    	yeardata=null;
	    	loadData(value,$("#target_type").val());
	    }
	 });
	option1 = {
		    title: {text: '',x:'center'},
		    tooltip: {trigger: 'axis'},
		    legend: {data:['月末存栏数', '能繁殖母畜'],top:30},
		    toolbox: {
		        feature: {saveAsImage: {}}
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: months
		    },
		    yAxis: {
	            type: 'value',name: '单位(万头)',min: 0,axisLabel: {formatter: '{value}'}
	        },
		    series: [
		        {
		            name:'月末存栏数',
		            type:'line',
		            data:[]
		        },
		        {
		            name:'能繁殖母畜',
		            type:'line',
		            data:[]
		        }
		    ]
	};
	/**产仔情况图***/
	option2 = {
		title: {text: '',x:'center'},
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {type: 'cross',crossStyle: {color: '#999'}}
	    },
	    toolbox: {
	        feature: {saveAsImage: {}}
	    },
	    legend: {
	        data:['产仔数','成活数','成活率','去年同期成活率'],top:30
	    },
	    xAxis: [
	        {type: 'category',data: months}
	    ],
	    yAxis: [
	        {
	            type: 'value',
	            name: '单位(万头)',
	            axisLabel: {formatter: '{value}'}
	        },
	        {
	            type: 'value',
	            name: '成活率(单位:%)',
	            axisLabel: {formatter: '{value}'}
	        }
	    ],
	    series: [
	        {
	            name:'产仔数',
	            type:'bar',
	            data:[]
	        },
	        {
	            name:'成活数',
	            type:'bar',
	            data:[]
	        },
	        {
	            name:'成活率',
	            type:'line',
	            yAxisIndex: 1,
	            data:[]
	        },
	        {
	            name:'去年同期成活率',
	            type:'line',
	            yAxisIndex: 1,
	            data:[]
	        }
	    ]
	};
	/**损亡情况图***/
	option3 = {
		title: {text: '',x:'center'},
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {type: 'cross',crossStyle: {color: '#999'}}
	    },
	    toolbox: {
	        feature: {saveAsImage: {}}
	    },
	    legend: {
	        data:['损亡数','去年同期','损亡率','去年同期损亡率'],top:30
	    },
	    xAxis: [
	        {type: 'category',data: months}
	    ],
	    yAxis: [
	        {
	            type: 'value',
	            name: '单位(万头)',
	            axisLabel: {formatter: '{value}'}
	        },
	        {
	            type: 'value',
	            name: '损亡率(单位:%)',
	            axisLabel: {formatter: '{value}'}
	        }
	    ],
	    series: [
	        {
	            name:'损亡数',
	            type:'bar',
	            data:[]
	        },
	        {
	            name:'去年同期',
	            type:'bar',
	            data:[]
	        },
	        {
	            name:'损亡率',
	            type:'line',
	            yAxisIndex: 1,
	            data:[]
	        },
	        {
	            name:'去年同期损亡率',
	            type:'line',
	            yAxisIndex: 1,
	            data:[]
	        }
	    ]
	};
	//出栏情况图
	option4 = {
		    title: {text: '',x:'center'},
		    tooltip: {trigger: 'axis'},
		    legend: {data:['出栏数', '出售数'],top:30},
		    toolbox: {
		        feature: {saveAsImage: {}}
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: months
		    },
		    yAxis: {
	            type: 'value',name: '单位(万头)',min: 0,axisLabel: {formatter: '{value}'}
	        },
		    series: [
		        {
		            name:'出栏数',
		            type:'bar',
		            data:[]
		        },
		        {
		            name:'出售数',
		            type:'bar',
		            data:[]
		        }
		    ]
	};
	//出栏情况图
	option5 = {
		    title: {text: '',x:'center'},
		    tooltip: {trigger: 'axis'},
		    legend: {data:['肉产量', '奶产量','蛋产量','毛产量'],top:30},
		    toolbox: {
		        feature: {saveAsImage: {}}
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: months
		    },
		    yAxis: {
	            type: 'value',name: '单位(万吨)',min: 0,axisLabel: {formatter: '{value}'}
	        },
		    series: [
		        {
		            name:'肉产量',
		            type:'bar',
		            data:[]
		        },
		        {
		            name:'奶产量',
		            type:'bar',
		            data:[]
		        },
		        {
		            name:'蛋产量',
		            type:'bar',
		            data:[]
		        },
		        {
		            name:'毛产量',
		            type:'bar',
		            data:[]
		        }
		    ]
	};
	function init(){
		inittitle();
		
		chart1 = echarts.init(document.getElementById('chart1')); 
		chart1.setOption(option1); 
		
		chart2 = echarts.init(document.getElementById('chart2')); 
		chart2.setOption(option2); 
		
		chart3 = echarts.init(document.getElementById('chart3')); 
		chart3.setOption(option3); 
		
		chart4 = echarts.init(document.getElementById('chart4')); 
		chart4.setOption(option4); 
		
		chart5 = echarts.init(document.getElementById('chart5')); 
		chart5.setOption(option5); 
		loadData(curyear,$("#target_type").val());
	}
	
	function initdata(data,year,target_type){
		var tabledata1=[],tabledata2=[],tabledata3=[],tabledata4=[],tabledata5=[];
		option1.series[0].data=[];
		option1.series[1].data=[];
		
		option2.series[0].data=[];
		option2.series[1].data=[];
		option2.series[2].data=[];
		option2.series[3].data=[];
		
		option3.series[0].data=[];
		option3.series[1].data=[];
		option3.series[2].data=[];
		option3.series[3].data=[];
		
		option4.series[0].data=[];
		option4.series[1].data=[];
		
		option5.series[0].data=[];
		option5.series[1].data=[];
		option5.series[2].data=[];
		option5.series[3].data=[];
		if(data){
			var index=0,emonth;
			if(year!=curyear){
				emonth=parseInt(year+'12');
			}else{
				emonth=curmonth;
			}
			for (var i = year*100+1; i <= emonth; i++) {
				if(data[i] && data[i].length>0){
					for(var j=0;j<data[i].length;j++){
						if(target_type==data[i][j].fgid){
							option1.series[0].data[index]=data[i][j].surplus_size;
							option1.series[1].data[index]=data[i][j].female_size;
							tabledata1.push({ym:i,surplus_size:data[i][j].surplus_size,female_size:data[i][j].female_size});
							
							option2.series[0].data[index]=data[i][j].child_size;
							option2.series[1].data[index]=data[i][j].survival_size;
							option2.series[2].data[index]=data[i][j].chl;
							option2.series[3].data[index]=data[i][j].chl_tq;
							tabledata2.push({ym:i,child_size:data[i][j].child_size,survival_size:data[i][j].survival_size,chl:data[i][j].chl,chl_tq:data[i][j].chl_tq});
							
							option3.series[0].data[index]=data[i][j].death_size;
							option3.series[1].data[index]=data[i][j].sws_tq;
							option3.series[2].data[index]=data[i][j].swl;
							option3.series[3].data[index]=data[i][j].swl_tq;
							tabledata3.push({ym:i,death_size:data[i][j].death_size,sws_tq:data[i][j].sws_tq,swl:data[i][j].swl,swl_tq:data[i][j].swl_tq});
							
							option4.series[0].data[index]=data[i][j].maturity_size;
							option4.series[1].data[index]=data[i][j].sell_size;
							tabledata4.push({ym:i,maturity_size:data[i][j].maturity_size,sell_size:data[i][j].sell_size});
							
							option5.series[0].data[index]=data[i][j].meat_output;
							option5.series[1].data[index]=data[i][j].milk_output;
							option5.series[2].data[index]=data[i][j].egg_output;
							option5.series[3].data[index]=data[i][j].hair_output;
							tabledata5.push({ym:i,meat_output:data[i][j].meat_output,milk_output:data[i][j].milk_output,egg_output:data[i][j].egg_output,hair_output:data[i][j].hair_output});
							break;
						}
					}
				}
				index++;
			}
		}
		inittable1(tabledata1);
		inittable2(tabledata2);
		inittable3(tabledata3);
		inittable4(tabledata4);
		inittable5(tabledata5);
		dispose(chart1,'chart1',option1);
		dispose(chart2,'chart2',option2);
		dispose(chart3,'chart3',option3);
		dispose(chart4,'chart4',option4);
		dispose(chart5,'chart5',option5);
	}
	
	function inittable1(data){
		$("#tabletitle1").text($("#target_type").find("option:selected").text()+"存栏情况统计表");
		//表格渲染
		datatable=table.render({
			id:"datalist1",
		    elem: '#datalist1',
		    page:false,
		    limit:20,
		    height:300,
		    cols: [[ //表头
		      {field: 'ym', title: '月份',align:'center'},
		      {field: 'surplus_size', title: '月末存栏数',align:'center'},
			  {field: 'female_size', title: '能繁殖母畜',align:'center'}
		    ]],
		    data:data
		});
	}
	function inittable2(data){
		$("#tabletitle2").text($("#target_type").find("option:selected").text()+"产仔情况统计表");
		//表格渲染
		datatable=table.render({
			id:"datalist2",
		    elem: '#datalist2',
		    page:false,
		    limit:20,
		    height:300,
		    cols: [[ //表头
		      {field: 'ym', title: '月份',align:'center'},
		      {field: 'child_size', title: '产仔数',align:'center'},
			  {field: 'survival_size', title: '成活数',align:'center'},
			  {field: 'chl', title: '成活率',align:'center'},
			  {field: 'chl_tq', title: '去年同期',align:'center'}
		    ]],
		    data:data
		});
	}
	function inittable3(data){
		$("#tabletitle3").text($("#target_type").find("option:selected").text()+"成畜损亡情况统计表");
		//表格渲染
		datatable=table.render({
			id:"datalist3",
		    elem: '#datalist3',
		    page:false,
		    limit:20,
		    height:300,
		    cols: [[ //表头
		      {field: 'ym', title: '月份',align:'center'},
		      {field: 'death_size', title: '损亡数',align:'center'},
			  {field: 'sws_tq', title: '去年同期',align:'center'},
			  {field: 'swl', title: '损亡率',align:'center'},
			  {field: 'swl_tq', title: '去年同期',align:'center'}
		    ]],
		    data:data
		});
	}
	function inittable4(data){
		$("#tabletitle4").text($("#target_type").find("option:selected").text()+"出栏情况统计表");
		//表格渲染
		datatable=table.render({
			id:"datalist4",
		    elem: '#datalist4',
		    page:false,
		    limit:20,
		    height:300,
		    cols: [[ //表头
		      {field: 'ym', title: '月份',align:'center'},
		      {field: 'maturity_size', title: '出栏数',align:'center'},
			  {field: 'sell_size', title: '出售数',align:'center'}
		    ]],
		    data:data
		});
	}
	function inittable5(data){
		$("#tabletitle5").text($("#target_type").find("option:selected").text()+"畜产品生产情况统计表");
		//表格渲染
		datatable=table.render({
			id:"datalist5",
		    elem: '#datalist5',
		    page:false,
		    limit:20,
		    height:300,
		    cols: [[ //表头
		      {field: 'ym', title: '月份',align:'center'},
		      {field: 'meat_output', title: '肉产量',align:'center'},
			  {field: 'milk_output', title: '奶产量',align:'center'},
			  {field: 'egg_output', title: '蛋产量',align:'center'},
			  {field: 'hair_output', title: '毛产量',align:'center'}
		    ]],
		    data:data
		});
	}
	function loadData(year,target_type){
		if(!yeardata){
			$("#preloaderDiv").show();
			$.post("/animalsBreed/monthDataAnalysis", {year: year,target_type: target_type},function(data){
				$("#preloaderDiv").hide();
				if(data){
					yeardata=data;
					initdata(yeardata,year,target_type);
				}
			});
		}else{
			initdata(yeardata,year,target_type);
		}
	}
	
	form.on('select(target_type)', function(data){
		inittitle();
		loadData($("#year").val(),$("#target_type").val());
	});  
	function inittitle(){
		var title=$("#target_type").find("option:selected").text();
		option1.title.text= title+"存栏情况统计图";
		option2.title.text= title+"产仔情况统计图";
	}
	
	function dispose(chart,domid,option){
		chart.dispose();
		chart = echarts.init(document.getElementById(domid)); 
		chart.setOption(option); 
	}
	
	init();
});
