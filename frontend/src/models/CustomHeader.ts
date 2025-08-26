// Header to be used when we have a TableFilter
//
// - searchable - whether or not the column values can be searched
// - type - for searchable columns, the type of search to perform (only date supported - leave empty for other)
// - items - for searchable columns, if there is a subset to choose from
// - itemTitle - if items is set, what should be displayed to the user to select
// - itemValue - if items is set, what should be the value returned from selection
// - filterKey - for searchable columns, the property name in searchFilter to store the filter selection
export default class CustomHeader {
  readonly key: string | undefined;
  readonly value: any;
  readonly title: string | undefined;
  readonly fixed: boolean | undefined;
  readonly align?: "end" | "start" | "center" | undefined;
  readonly width?: string | number | undefined;
  readonly minWidth?: string | number | undefined;
  readonly maxWidth?: string | number | undefined;
  readonly nowrap?: boolean | undefined;
  readonly headerProps?: { readonly [x: string]: any } | undefined;
  readonly cellProps?: { readonly [x: string]: any } | undefined;
  readonly sortable?: boolean | undefined;
  readonly children?: readonly any[] | undefined;

  readonly searchable: boolean;
  readonly type: "date" | undefined;
  readonly items: any | undefined;
  readonly itemTitle: string | undefined;
  readonly itemValue: string | undefined;
  readonly filterKey: string;
}
