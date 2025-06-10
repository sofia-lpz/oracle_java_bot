import * as React from 'react';
import PropTypes from 'prop-types';
import Box from '@mui/material/Box';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import TableSortLabel from '@mui/material/TableSortLabel';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import { visuallyHidden } from '@mui/utils';
import { createTheme, ThemeProvider } from '@mui/material/styles';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#c6624b',
    },
    secondary: {
      main: '#c6624b',
    },
    background: {
      paper: '#272727',
      default: '#1d1d1d',
    },
    text: {
      primary: '#ffffff',
      secondary: '#d3d3d3',
    },
  },
  components: {
    MuiTableRow: {
      styleOverrides: {
        root: {
          '&:hover': {
            backgroundColor: '#3a3a3a !important',
          },
        },
      },
    },
    MuiTableHead: {
      styleOverrides: {
        root: {
          backgroundColor: '#333',
        },
      },
    },
    MuiTableCell: {
      styleOverrides: {
        head: {
          backgroundColor: '#333',
          color: '#ffffff',
          fontWeight: 'bold',
        },
        body: {
          borderBottom: '1px solid #444',
        },
      },
    },
    MuiTablePagination: {
      styleOverrides: {
        root: {
          color: '#ffffff',
        },
        selectIcon: {
          color: '#ffffff',
        },
      },
    },
    MuiTableSortLabel: {
      styleOverrides: {
        root: {
          color: '#ffffff',
          '&:hover': {
            color: '#c6624b',
          },
          '&.Mui-active': {
            color: '#c6624b',
          },
        },
        icon: {
          color: '#ffffff !important',
        },
      },
    },
  },
});


function descendingComparator(a, b, orderBy) {
  if (b[orderBy] < a[orderBy]) {
    return -1;
  }
  if (b[orderBy] > a[orderBy]) {
    return 1;
  }
  return 0;
}

function getComparator(order, orderBy) {
  return order === 'desc'
    ? (a, b) => descendingComparator(a, b, orderBy)
    : (a, b) => -descendingComparator(a, b, orderBy);
}

const headCells = [
  {
    id: 'title',
    numeric: false,
    disablePadding: false,
    label: 'Titulo de la Tarea',
  },
  {
    id: 'username',
    numeric: false,
    disablePadding: false,
    label: 'Developer',
  },
  {
    id: 'estimatedHours',
    numeric: true,
    disablePadding: false,
    label: 'Horas Estimadas',
  },
  {
    id: 'realHours',
    numeric: true,
    disablePadding: false,
    label: 'Horas Reales',
  },
];

function EnhancedTableHead(props) {
  const { order, orderBy, onRequestSort } = props;
  const createSortHandler = (property) => (event) => {
    onRequestSort(event, property);
  };

  return (
    <TableHead>
      <TableRow>
        {headCells.map((headCell) => (
          <TableCell
            key={headCell.id}
            align={headCell.numeric ? 'right' : 'left'}
            padding="normal"
            sortDirection={orderBy === headCell.id ? order : false}
          >
            <TableSortLabel
              active={orderBy === headCell.id}
              direction={orderBy === headCell.id ? order : 'asc'}
              onClick={createSortHandler(headCell.id)}
            >
              {headCell.label}
              {orderBy === headCell.id ? (
                <Box component="span" sx={visuallyHidden}>
                  {order === 'desc' ? 'sorted descending' : 'sorted ascending'}
                </Box>
              ) : null}
            </TableSortLabel>
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );
}

EnhancedTableHead.propTypes = {
  onRequestSort: PropTypes.func.isRequired,
  order: PropTypes.oneOf(['asc', 'desc']).isRequired,
  orderBy: PropTypes.string.isRequired,
};

function EnhancedTableToolbar(props) {
  const { title } = props;
  return (
    <Toolbar
      sx={{
        pl: { sm: 2 },
        pr: { xs: 1, sm: 1 },
      }}
    >
      <Typography
        sx={{ flex: '1 1 100%', color: '#ffffff', fontWeight: 'bold' }}
        variant="h6"
        id="tableTitle"
        component="div"
      >
        {title || 'Tasks'}
      </Typography>
    </Toolbar>
  );
}

EnhancedTableToolbar.propTypes = {
  title: PropTypes.string,
};

export default function TaskTable({ tasks, title }) {
  const [order, setOrder] = React.useState('asc');
  const [orderBy, setOrderBy] = React.useState('title');
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(5);

  const handleRequestSort = (event, property) => {
    const isAsc = orderBy === property && order === 'asc';
    setOrder(isAsc ? 'desc' : 'asc');
    setOrderBy(property);
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  // Avoid a layout jump when reaching the last page with empty rows.
  const emptyRows =
    page > 0 ? Math.max(0, (1 + page) * rowsPerPage - tasks.length) : 0;

  const visibleRows = React.useMemo(
    () =>
      [...tasks]
        .sort(getComparator(order, orderBy))
        .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage),
    [order, orderBy, page, rowsPerPage, tasks],
  );

return (
    <ThemeProvider theme={darkTheme}>
      <Box sx={{ width: '100%' }}>
        <Paper 
          sx={{ 
            width: '100%', 
            mb: 2, 
            backgroundColor: '#272727',
            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
          }}
        >
          <EnhancedTableToolbar title={title} />
          <TableContainer>
            <Table
              sx={{ minWidth: 750 }}
              aria-labelledby="tableTitle"
              size="medium"
            >
              <EnhancedTableHead
                order={order}
                orderBy={orderBy}
                onRequestSort={handleRequestSort}
              />
              <TableBody>
                {visibleRows.map((row, index) => {
                  return (
                    <TableRow
                      hover
                      tabIndex={-1}
                      key={row.id}
                      sx={{ '&:hover': { backgroundColor: '#3a3a3a' } }}
                    >
                      <TableCell>{row.title}</TableCell>
                      <TableCell>{row.user?.name || 'Unassigned'}</TableCell>
                      <TableCell align="right">{row.estimatedHours || 0}</TableCell>
                      <TableCell align="right">{row.realHours || 0}</TableCell>
                    </TableRow>
                  );
                })}
                {emptyRows > 0 && (
                  <TableRow
                    style={{
                      height: 53 * emptyRows,
                    }}
                  >
                    <TableCell colSpan={4} />
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
          <TablePagination
            rowsPerPageOptions={[5, 10, 25]}
            component="div"
            count={tasks.length}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
            sx={{
              color: '#ffffff',
              '& .MuiSvgIcon-root': {
                color: '#ffffff',
              },
            }}
          />
        </Paper>
      </Box>
    </ThemeProvider>
  );
}

TaskTable.propTypes = {
  tasks: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
      title: PropTypes.string.isRequired,
      user: PropTypes.shape({
        name: PropTypes.string,
      }),
      estimatedHours: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
      realHours: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    })
  ).isRequired,
  title: PropTypes.string,
};

// Default props
TaskTable.defaultProps = {
  tasks: [],
  title: 'Tasks',
};