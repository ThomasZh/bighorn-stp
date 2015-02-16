package net.younguard.bighorn.broadcast.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.younguard.bighorn.broadcast.domain.Page;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class PaginationHelper<E>
{
	public Page<E> fetchPage(final JdbcTemplate jt, final String sqlCountRows, final String sqlFetchRows,
			final Object args[], final int pageNum, final int pageSize, final ParameterizedRowMapper<E> rowMapper)
	{
		// determine how many rows are available
		final int rowCount = jt.queryForInt(sqlCountRows, args);

		// calculate the number of pages
		int pageCount = rowCount / pageSize;
		if (rowCount > pageSize * pageCount) {
			pageCount++;
		}

		// create the page object
		final Page<E> page = new Page<E>();
		page.setPageNumber(pageNum);
		page.setPagesAvailable(pageCount);

		// fetch a single page of results
		final int startRow = (pageNum - 1) * pageSize;
		jt.query(sqlFetchRows, args, new ResultSetExtractor()
		{
			public Object extractData(ResultSet rs)
					throws SQLException, DataAccessException
			{
				final List<E> pageItems = page.getPageItems();
				int currentRow = 0;
				while (rs.next() && currentRow < startRow + pageSize) {
					if (currentRow >= startRow) {
						pageItems.add(rowMapper.mapRow(rs, currentRow));
					}
					currentRow++;
				}
				return page;
			}
		});
		return page;
	}
}