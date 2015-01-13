function addDetail(oneWordBean) {
	var wordInfo = "<div>" + " id:" + oneWordBean.id + " content:"
			+ oneWordBean.content + " comment:" + oneWordBean.comment + " hit:"
			+ oneWordBean.hit + " rank:" + oneWordBean.rank + "</div>";
	$("#wordsDiv").append(wordInfo);
}

/*
*函数功能：从href获得参数
*sHref:   http://www.artfh.com/arg.htm?arg1=d&arg2=re
*sArgName:arg1, arg2
*return:    the value of arg. d, re
*/
function GetArgsFromHref(sHref, sArgName)
{
      var args    = sHref.split("?");
      var retval = "";
    
      if(args[0] == sHref) /*参数为空*/
      {
           return retval; /*无需做任何处理*/
      }  
      var str = args[1];
      args = str.split("&");
      for(var i = 0; i < args.length; i ++)
      {
          str = args[i];
          var arg = str.split("=");
          if(arg.length <= 1) continue;
          if(arg[0] == sArgName) retval = arg[1]; 
      }
      return retval;
}

function getSimilarWords(id) {
	var pattern = /(id=\".+_content\")/ig;
	var rowData = jQuery("#grid-table")
			.getRowData(id);
	var rowDataContent = rowData.content;
	if (rowDataContent.toLowerCase()
			.indexOf('<input') >= 0) {
		rowDataContent = $(
				'#' + id
						+ '_content')
				.val();
	}
	window.open("similarWords.html?id=" + id + "&word=" + rowDataContent);
}


var lastsel2;
$(document)
		.ready(
				function() {
					var ignoreEasy = GetArgsFromHref(window.location.href, "ignoreEasy");
					jQuery("#grid-table")
							.jqGrid(
									{
										url : "play.do?action=getWordDicList&ignoreEasy=" + ignoreEasy,
										serializeEditData : function(data) {
											return $.param($.extend({}, data, {
												id : 0
											}));
										},

										datatype : "json",
										height : 470,
										colNames : [ 'id', 'content',
												'comment', 'hit', 'rank',
												'act', 'createDate', 'lastUpt' ],
										colModel : [
												{
													name : 'id',
													index : 'id',
													width : 50,
													sorttype : "int",
													editable : false
												},
												{
													name : 'content',
													index : 'content',
													width : 150,
													editable : true,
													editoptions : {
														size : "20",
														maxlength : "30"
													}
												},
												{
													name : 'comment',
													index : 'comment',
													width : 250,
													editable : true,
													editoptions : {
														size : "20",
														maxlength : "30"
													},
													edittype : "textarea",
													editoptions : {
														rows : "2",
														cols : "10"
													}
												},
												{
													name : 'hit',
													index : 'hit',
													width : 90,
													sorttype : "int",
													editable : true
												},
												{
													name : 'rank',
													index : 'rank',
													width : 100,
													sorttype : "int",
													editable : true,
													edittype : "select",
													editoptions : {
														value : "1:Easy;2:Master;3:Familiar;4:NotFamiliar;5:Hard;-1:SeldomUsed;-2:Ignore;0:NotSet;-10:Broadcasted"
													}
												}, {
													name : 'act',
													index : 'act',
													width : 150,
													sortable : false
												}, {
													name : 'createDate',
													index : 'createDate',
													width : 150,
													sortable : true
												}, {
													name : 'lastUpt',
													index : 'lastUpt',
													width : 150,
													sortable : true
												} ],
										onSelectRow : function(id) {
											if (id && id !== lastsel2) {
												jQuery('#grid-table').jqGrid(
														'restoreRow', lastsel2);
												jQuery('#grid-table').jqGrid(
														'editRow', id, true);
												lastsel2 = id;
											}
										},

										viewrecords : true,
										rowNum : 20,
										// rowList:[10,20,30],
										pager : "#grid-pager",
										altRows : true,
										// toppager: true,

										multiselect : true,
										// multikey: "ctrlKey",
										multiboxonly : true,

										gridComplete : function() {
											var ids = jQuery("#grid-table")
													.jqGrid('getDataIDs');
											// alert("ids:" + ids);
											for (var i = 0; i < ids.length; i++) {
												var cl = ids[i];
												be = "<input style='height:22px;width:20px;' type='button' value='E' onclick=\"jQuery('#grid-table').editRow('"
														+ cl + "');\"  />";
												se = "<input style='height:22px;width:20px;' type='button' value='S' onclick=\"jQuery('#grid-table').saveRow('"
														+ cl + "');\"  />";
												ce = "<input style='height:22px;width:20px;' type='button' value='C' onclick=\"jQuery('#grid-table').restoreRow('"
														+ cl + "');\" />";
												similarBtn = "<input style='height:22px;width:50px;' type='button' value='Similar' onclick=\"getSimilarWords('"
													+ cl + "');\" />";
												jQuery("#grid-table").jqGrid(
														'setRowData',
														ids[i],
														{
															act : ce + "&nbsp;"
																	+ be
																	+ "&nbsp;"
																	+ se
																	+ "&nbsp;"
																	+ similarBtn
														});
											}
										},

										editurl : "play.do?action=addOrUpdateWord",
										caption : "Words List",
										subGrid : true,
										subGridRowExpanded : function(
												subgrid_id, row_id) {
											// we pass two parameters
											// subgrid_id is a id of the div tag
											// created whitin a table data
											// the id of this elemenet is a
											// combination of the "sg_" + id of
											// the row
											// the row_id is the id of the row
											// If we wan to pass additinal
											// parameters to the url we can use
											// a method getRowData(row_id) -
											// which returns associative array
											// in type name-value
											// here we can easy construct the
											// flowing
											var pattern = /(id=\".+_content\")/ig;
											var rowData = jQuery("#grid-table")
													.getRowData(row_id);
											var rowDataContent = rowData.content;
											if (rowDataContent.toLowerCase()
													.indexOf('<input') >= 0) {
												rowDataContent = $(
														'#' + row_id
																+ '_content')
														.val();
											}

											// alert("rowData:" + rowData);
											var subgrid_table_id, pager_id;
											subgrid_table_id = subgrid_id
													+ "_t";
											pager_id = "p_" + subgrid_table_id;
											$("#" + subgrid_id)
													.html(
															"<table id='"
																	+ subgrid_table_id
																	+ "' class='scroll'></table><div id='"
																	+ pager_id
																	+ "' class='scroll'></div>");
											jQuery("#" + subgrid_table_id)
													.jqGrid(
															{
																url : "play.do?action=getSenList&word="
																		+ rowDataContent,
																datatype : "json",
																colNames : [
																		'ID',
																		'Content',
																		'Rank' ],
																colModel : [
																		{
																			name : "id",
																			index : "id",
																			width : 80,
																			key : true
																		},
																		{
																			name : "content",
																			index : "content",
																			width : 1000
																		},
																		{
																			name : "rank",
																			index : "rank",
																			width : 70
																		} ],
																rowNum : 20,
																pager : pager_id,
																sortname : 'num',
																sortorder : "asc",
																height : '100%'
															});
											jQuery("#" + subgrid_table_id)
													.jqGrid('navGrid',
															"#" + pager_id, {
																edit : false,
																add : false,
																del : false
															})
										},
										subGridRowColapsed : function(
												subgrid_id, row_id) { // this
																		// function
																		// is
																		// called
																		// before
																		// removing
																		// the
																		// data
																		// //var
																		// subgrid_table_id;
																		// //subgrid_table_id
																		// =
																		// subgrid_id+"_t";
																		// //jQuery("#"+subgrid_table_id).remove();
																		// }
										}

									// ,autowidth: true,

									/**
									 * , grouping:true, groupingView : {
									 * groupField : ['name'], groupDataSorted :
									 * true, plusicon : 'fa fa-chevron-down
									 * bigger-110', minusicon : 'fa
									 * fa-chevron-up bigger-110' }, caption:
									 * "Grouping"
									 */

									});

					jQuery("#grid-table").jqGrid('navGrid', "#grid-pager", {
						edit : true,
						add : true,
						del : true
					}, {
						closeAfterEdit : true
					}, {
						closeAfterAdd : true
					});
				});
